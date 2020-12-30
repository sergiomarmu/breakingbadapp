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
    suspend fun retrieveCharactersFlow(
        userActionMutableStateFlow: MutableStateFlow<Int>
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
    private val localInteractor: LocalInteractor
) : NetworkInteractor {
    override suspend fun retrieveCharactersFlow(
        userActionMutableStateFlow: MutableStateFlow<Int>
    ): Flow<CharacterState> =
        userActionMutableStateFlow
            .flatMapLatest {
                flow {
                    emit(
                        networkRepository.retrieveCharacters().toCharacterModel()
                    )
                }.flatMapLatest { charactersModel ->
                    localInteractor
                        .retrieveFavouriteCharactersFlow()
                        .flatMapLatest { listFavourites ->
                            for (characterModel in charactersModel)
                                for (listFavourite in listFavourites)
                                    if (characterModel.charId == listFavourite.charId)
                                        characterModel.isFavourite = listFavourite.isFavourite
                            flowOf(CharacterState.Success(charactersModel))
                        }
                }.catch<CharacterState> { error ->
                    emit(
                        when (error) {
                            is DataException.Unparseable,
                            is DataException.Unexpected ->
                                CharacterState.Failure.Unexpected(error.message)
                            is DataException.Network -> CharacterState.Failure.NoInternet(
                                error.message
                            )
                            else -> CharacterState.Failure.Unexpected(error.message)
                        }
                    )
                }
            }


    /**
     *
     *  combine(
    flow {
    emit(
    networkRepository.retrieveCharacters().toCharacterModel()
    )
    },
    userRefreshActionMutableStateFlow,
    ) { charactersModel, userRefresh, userAdd ->
    return@combine userRefresh to userAdd to charactersModel
    }.flatMapLatest { result ->
    // We ignore userAction value because we only want to know when user do an action for refresh recyclerview
    val (userAction, charactersModel) = result
    localInteractor
    .retrieveFavouriteCharactersFlow()
    .flatMapLatest { listFavourites ->
    for (characterModel in charactersModel)
    for (listFavourite in listFavourites)
    if (characterModel.charId == listFavourite.charId)
    characterModel.isFavourite = listFavourite.isFavourite
    flowOf(CharacterState.Success(charactersModel))
    }
    }.catch<CharacterState> { error ->
    emit(
    when (error) {
    is DataException.Unparseable,
    is DataException.Unexpected ->
    CharacterState.Failure.Unexpected(error.message)
    is DataException.Network -> CharacterState.Failure.NoInternet(
    error.message
    )
    else -> CharacterState.Failure.Unexpected(error.message)
    }
    )
    }
     */
}