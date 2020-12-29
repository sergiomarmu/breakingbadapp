package com.sermarmu.domain.interactor

import com.sermarmu.data.handler.DataException
import com.sermarmu.data.repository.NetworkRepository
import com.sermarmu.domain.interactor.NetworkInteractor.CharacterState
import com.sermarmu.domain.model.CharacterModel
import com.sermarmu.domain.model.toCharacterModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

interface NetworkInteractor {
    suspend fun retrieveCharacters(): Flow<CharacterState>


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

class NetworkInteractorImpl @Inject constructor(
    private val networkRepository: NetworkRepository
) : NetworkInteractor {
    override suspend fun retrieveCharacters(): Flow<CharacterState> =
        flow {
            emit(networkRepository.retrieveCharacters().toCharacterModel())
        }.map {
            CharacterState.Success(it)
        }.distinctUntilChanged()
            .catch<CharacterState> { error ->
                when (error) {
                    is DataException.Network -> CharacterState.Failure.NoInternet(
                        error.message
                    )
                    is DataException.Unparseable,
                    is DataException.Unexpected ->
                        CharacterState.Failure.Unexpected(error.message)
                }
            }
}