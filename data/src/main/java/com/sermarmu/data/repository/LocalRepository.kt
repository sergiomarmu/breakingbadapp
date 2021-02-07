package com.sermarmu.data.repository

import com.sermarmu.data.entity.FavouriteCharacter
import com.sermarmu.data.entity.toFavouriteCharacter
import com.sermarmu.data.entity.toFavouriteCharacters
import com.sermarmu.data.source.local.LocalSource
import com.sermarmu.data.source.local.io.input.FavouriteCharacterInput
import javax.inject.Inject

interface LocalRepository {
    suspend fun retrieveFavouriteCharacters(): Set<FavouriteCharacter>

    suspend fun retrieveFavouriteCharacter(
        charId: Int
    ): FavouriteCharacter?

    suspend fun insertFavouriteCharacters(
        vararg favoriteCharacter: FavouriteCharacter
    )

    suspend fun updateFavoriteCharacter(
        favoriteCharacter: FavouriteCharacter
    )

    suspend fun deleteFavouriteCharacter(
        favoriteCharacter: FavouriteCharacter
    )
}

class LocalRepositoryImpl @Inject constructor(
    private val localSource: LocalSource
) : LocalRepository {
    override suspend fun retrieveFavouriteCharacters(): Set<FavouriteCharacter> =
        localSource.retrieveFavouriteCharacters().toFavouriteCharacters()

    override suspend fun retrieveFavouriteCharacter(
        charId: Int
    ): FavouriteCharacter? =
        localSource.retrieveFavouriteCharacter(
            charId = charId
        )?.toFavouriteCharacter()

    override suspend fun insertFavouriteCharacters(
        vararg favoriteCharacter: FavouriteCharacter
    ) {
        localSource.insertFavouriteCharacters(
            favoriteCharacter = favoriteCharacter.map {
                FavouriteCharacterInput(
                    charId = it.charId,
                    name = it.name,
                    isFavourite = it.isFavourite
                )
            }.toTypedArray()
        )
    }

    override suspend fun updateFavoriteCharacter(
        favoriteCharacter: FavouriteCharacter
    ) {
        localSource.updateFavoriteCharacter(
            favoriteCharacter = FavouriteCharacterInput(
                charId = favoriteCharacter.charId,
                name = favoriteCharacter.name,
                isFavourite = favoriteCharacter.isFavourite
            )
        )
    }

    override suspend fun deleteFavouriteCharacter(
        favoriteCharacter: FavouriteCharacter
    ) {
        localSource.deleteFavouriteCharacter(
            favoriteCharacter = FavouriteCharacterInput(
                charId = favoriteCharacter.charId,
                name = favoriteCharacter.name,
                isFavourite = favoriteCharacter.isFavourite
            )
        )
    }
}