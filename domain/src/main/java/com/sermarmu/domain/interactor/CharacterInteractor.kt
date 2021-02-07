package com.sermarmu.domain.interactor

import com.sermarmu.data.entity.FavouriteCharacter
import com.sermarmu.data.handler.DataException
import com.sermarmu.data.source.local.io.input.FavouriteCharacterInput
import com.sermarmu.domain.interactor.CharacterInteractor.CharacterState
import com.sermarmu.domain.model.CharacterModel
import com.sermarmu.domain.model.FavouriteCharacterModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

interface CharacterInteractor {
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

    suspend fun retrieveCharactersFlow(
        userActionMutableStateFlow: MutableStateFlow<Int>
    ): Flow<CharacterState>

    suspend fun addFavouriteCharacter(
        characterModel: CharacterModel
    )
}

@ExperimentalCoroutinesApi
class CharacterInteractorImpl @Inject constructor(
    private val networkInteractor: NetworkInteractor,
    private val localInteractor: LocalInteractor
) : CharacterInteractor {

    override suspend fun retrieveCharactersFlow(
        userActionMutableStateFlow: MutableStateFlow<Int>
    ): Flow<CharacterState> =
        userActionMutableStateFlow
            .flatMapLatest {
                networkInteractor.retrieveCharacters()
                    .flatMapLatest { charactersModel ->
                        localInteractor
                            .retrieveFavouriteCharactersFlow()
                            .flatMapLatest { listFavourites ->
                                val characters = charactersModel.map {
                                    it.charId to it
                                }.toMap()

                                for (favourite in listFavourites)
                                    with(favourite) {
                                        if (characters.containsKey(charId))
                                            characters.getValue(charId).isFavourite =
                                                favourite.isFavourite
                                    }
                                flowOf(CharacterState.Success(characters.values.toSet()))
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

    override suspend fun addFavouriteCharacter(
        characterModel: CharacterModel
    ) {
        localInteractor.retrieveFavouriteCharacterFlow(
            charId = characterModel.charId
        ).first()
            .let {
                if (it != null)
                    localInteractor.updateFavoriteCharacter(
                        FavouriteCharacterModel(
                            charId = characterModel.charId,
                            name = characterModel.name,
                            isFavourite = characterModel.isFavourite
                        )
                    )
                else localInteractor.insertFavouriteCharacters(
                    FavouriteCharacterModel(
                        charId = characterModel.charId,
                        name = characterModel.name,
                        isFavourite = true
                    )
                )
            }
    }
}