package com.sermarmu.domain.interactor

import com.sermarmu.data.repository.LocalRepository
import com.sermarmu.data.source.local.io.input.FavouriteCharacterInput
import com.sermarmu.data.source.local.io.output.FavouriteCharacterOutput
import com.sermarmu.domain.model.CharacterModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


interface LocalInteractor {
    suspend fun retrieveFavouriteCharactersFlow(): Flow<List<FavouriteCharacterOutput>>

    suspend fun retrieveFavouriteCharacterFlow(
        charId: Int
    ): Flow<FavouriteCharacterOutput?>

    suspend fun insertFavouriteCharacters(
        vararg favoriteCharacter: FavouriteCharacterInput
    )

    suspend fun updateFavoriteCharacter(
        favoriteCharacter: FavouriteCharacterInput
    )

    suspend fun deleteFavouriteCharacter(
        favoriteCharacter: FavouriteCharacterInput
    )

    suspend fun addFavouriteCharacter(
        characterModel: CharacterModel
    )
}

class LocalInteractorImpl @Inject constructor(
    private val localRepository: LocalRepository
) : LocalInteractor {
    override suspend fun retrieveFavouriteCharactersFlow(): Flow<List<FavouriteCharacterOutput>> =
        flow {
            emit(localRepository.retrieveFavouriteCharacters())
        }

    override suspend fun retrieveFavouriteCharacterFlow(
        charId: Int
    ): Flow<FavouriteCharacterOutput?> =
        flow {
            emit(
                localRepository.retrieveFavouriteCharacter(
                    charId = charId
                )
            )
        }

    override suspend fun insertFavouriteCharacters(
        vararg favoriteCharacter: FavouriteCharacterInput
    ) {
        localRepository.insertFavouriteCharacters(
            favoriteCharacter = *favoriteCharacter
        )
    }

    override suspend fun updateFavoriteCharacter(
        favoriteCharacter: FavouriteCharacterInput
    ) {
        localRepository.updateFavoriteCharacter(
            favoriteCharacter = favoriteCharacter
        )
    }

    override suspend fun deleteFavouriteCharacter(
        favoriteCharacter: FavouriteCharacterInput
    ) {
        localRepository.deleteFavouriteCharacter(
            favoriteCharacter = favoriteCharacter
        )
    }

    override suspend fun addFavouriteCharacter(
        characterModel: CharacterModel
    ) {
        retrieveFavouriteCharacterFlow(
            charId = characterModel.charId
        ).first()
            .let {
                if (it != null)
                    updateFavoriteCharacter(
                        FavouriteCharacterInput(
                            charId = characterModel.charId,
                            name = characterModel.name,
                            isFavourite = characterModel.isFavourite
                        )
                    )
                else insertFavouriteCharacters(
                    FavouriteCharacterInput(
                        charId = characterModel.charId,
                        name = characterModel.name,
                        isFavourite = true
                    )
                )
            }
    }
}