package com.sermarmu.domain.interactor

import com.sermarmu.data.handler.DataException
import com.sermarmu.data.repository.LocalRepository
import com.sermarmu.data.repository.NetworkRepository
import com.sermarmu.domain.interactor.NetworkInteractor.CharacterState
import com.sermarmu.domain.model.CharacterModel
import com.sermarmu.domain.model.toCharacterModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
interface NetworkInteractor {
    suspend fun retrieveCharactersFlow(
        userRefreshActionMutableStateFlow: MutableStateFlow<Int>,
        userAddFavouriteCharacterMutableStateFlow: MutableStateFlow<Int>
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
    private val networkRepository: NetworkRepository,
    private val localRepository: LocalRepository
) : NetworkInteractor {
    override suspend fun retrieveCharactersFlow(
        userRefreshActionMutableStateFlow: MutableStateFlow<Int>,
        userAddFavouriteCharacterMutableStateFlow: MutableStateFlow<Int>
    ): Flow<CharacterState> =
        combine(
            flowOf(networkRepository.retrieveCharacters().toCharacterModel()),
            userRefreshActionMutableStateFlow,
            userAddFavouriteCharacterMutableStateFlow
        ) { charactersModel, userRefresh, userAdd ->
            return@combine userRefresh to userAdd to charactersModel
        }.distinctUntilChanged()
            .flatMapLatest { result ->
                // We ignore userAction value because we only want to know when user do an action for refresh recyclerview
                val (userAction, charactersModel) = result

                flow {
                    emit(localRepository.retrieveFavouriteCharacters())
                }.map { listFavourites ->
                    for (characterModel in charactersModel)
                        for (listFavourite in listFavourites)
                            if (characterModel.charId == listFavourite.charId)
                                characterModel.isFavourite = listFavourite.isFavourite
                    CharacterState.Success(charactersModel)
                }
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