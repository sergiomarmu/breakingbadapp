package com.sermarmu.domain.interactor

import com.sermarmu.data.handler.DataException
import com.sermarmu.data.repository.NetworkRepository
import com.sermarmu.domain.interactor.NetworkInteractor.CharacterState
import com.sermarmu.domain.model.CharacterModel
import com.sermarmu.domain.model.toCharacterModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
interface NetworkInteractor {
    suspend fun retrieveCharacters(
        userRefreshActionMutableStateFlow: MutableStateFlow<Int>
    ): Flow<CharacterState>


    sealed class CharacterState {
        data class Success(
            val characters: Set<CharacterModel>
        ) : CharacterState()

        sealed class Failure : CharacterState() {
            data class NoInternet(
                val message: String? = null
            ) : Failure()

            data class Unexpected(
                val message: String? = null
            ) : Failure()
        }
    }
}

@ExperimentalCoroutinesApi
class NetworkInteractorImpl @Inject constructor(
    private val networkRepository: NetworkRepository
) : NetworkInteractor {
    override suspend fun retrieveCharacters(
        userRefreshActionMutableStateFlow: MutableStateFlow<Int>
    ): Flow<CharacterState> =
        userRefreshActionMutableStateFlow.flatMapLatest {
            flow {
                emit(networkRepository.retrieveCharacters().toCharacterModel())
            }.map {
                CharacterState.Success(it)
            }.distinctUntilChanged()
                .catch<CharacterState> { error ->
                    emit(
                        when (error) {
                            is DataException.Network -> CharacterState.Failure.NoInternet(
                                error.message
                            )
                            is DataException.Unparseable,
                            is DataException.Unexpected ->
                                CharacterState.Failure.Unexpected(error.message)
                            else -> CharacterState.Failure.Unexpected(error.message)
                        }
                    )
                }
        }

}