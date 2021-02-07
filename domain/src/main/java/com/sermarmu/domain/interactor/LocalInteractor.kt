package com.sermarmu.domain.interactor

import com.sermarmu.data.entity.FavouriteCharacter
import com.sermarmu.data.repository.LocalRepository
import com.sermarmu.data.source.local.io.input.FavouriteCharacterInput
import com.sermarmu.data.source.local.io.output.FavouriteCharacterOutput
import com.sermarmu.domain.model.CharacterModel
import com.sermarmu.domain.model.FavouriteCharacterModel
import com.sermarmu.domain.model.toFavouriteCharacterModel
import com.sermarmu.domain.model.toFavouriteCharactersModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


interface LocalInteractor {
    suspend fun retrieveFavouriteCharactersFlow(): Flow<Set<FavouriteCharacterModel>>

    suspend fun retrieveFavouriteCharacterFlow(
        charId: Int
    ): Flow<FavouriteCharacterModel?>

    suspend fun insertFavouriteCharacters(
        vararg favoriteCharacter: FavouriteCharacterModel
    )

    suspend fun updateFavoriteCharacter(
        favoriteCharacter: FavouriteCharacterModel
    )

    suspend fun deleteFavouriteCharacter(
        favoriteCharacter: FavouriteCharacterModel
    )
}

class LocalInteractorImpl @Inject constructor(
    private val localRepository: LocalRepository
) : LocalInteractor {
    override suspend fun retrieveFavouriteCharactersFlow(): Flow<Set<FavouriteCharacterModel>> =
        flow {
            emit(
                localRepository.retrieveFavouriteCharacters()
                    .toFavouriteCharactersModel()
            )
        }

    override suspend fun retrieveFavouriteCharacterFlow(
        charId: Int
    ): Flow<FavouriteCharacterModel?> =
        flow {
            emit(
                localRepository.retrieveFavouriteCharacter(
                    charId = charId
                )?.toFavouriteCharacterModel()
            )
        }

    override suspend fun insertFavouriteCharacters(
        vararg favoriteCharacterModel: FavouriteCharacterModel
    ) {
        localRepository.insertFavouriteCharacters(
            favoriteCharacter = favoriteCharacterModel.map {
                FavouriteCharacter(
                    charId = it.charId,
                    name = it.name,
                    isFavourite = it.isFavourite
                )
            }.toTypedArray()
        )
    }

    override suspend fun updateFavoriteCharacter(
        favoriteCharacterModel: FavouriteCharacterModel
    ) {
        localRepository.updateFavoriteCharacter(
            favoriteCharacter = FavouriteCharacter(
                charId = favoriteCharacterModel.charId,
                name = favoriteCharacterModel.name,
                isFavourite = favoriteCharacterModel.isFavourite
            )
        )
    }

    override suspend fun deleteFavouriteCharacter(
        favoriteCharacterModel: FavouriteCharacterModel
    ) {
        localRepository.deleteFavouriteCharacter(
            favoriteCharacter = FavouriteCharacter(
                charId = favoriteCharacterModel.charId,
                name = favoriteCharacterModel.name,
                isFavourite = favoriteCharacterModel.isFavourite
            )
        )
    }
}