package com.sermarmu.data.repository

import com.sermarmu.data.source.local.LocalSource
import com.sermarmu.data.source.local.io.input.FavouriteCharacterInput
import com.sermarmu.data.source.local.io.output.FavouriteCharacterOutput
import javax.inject.Inject

interface LocalRepository {
    suspend fun retrieveFavouriteCharacters(): List<FavouriteCharacterOutput>

    suspend fun retrieveFavouriteCharacter(
        charId: Int
    ): FavouriteCharacterOutput?

    suspend fun insertFavouriteCharacters(
        vararg favoriteCharacter: FavouriteCharacterInput
    )

    suspend fun updateFavoriteCharacter(
        favoriteCharacter: FavouriteCharacterInput
    )

    suspend fun deleteFavouriteCharacter(
        favoriteCharacter: FavouriteCharacterInput
    )
}

class LocalRepositoryImpl @Inject constructor(
    private val localSource: LocalSource
) : LocalRepository {
    override suspend fun retrieveFavouriteCharacters(): List<FavouriteCharacterOutput> =
        localSource.retrieveFavouriteCharacters()

    override suspend fun retrieveFavouriteCharacter(
        charId: Int
    ): FavouriteCharacterOutput? =
        localSource.retrieveFavouriteCharacter(
            charId = charId
        )

    override suspend fun insertFavouriteCharacters(
        vararg favoriteCharacter: FavouriteCharacterInput
    ) {
        localSource.insertFavouriteCharacters(
            favoriteCharacter = *favoriteCharacter
        )
    }

    override suspend fun updateFavoriteCharacter(
        favoriteCharacter: FavouriteCharacterInput
    ) {
        localSource.updateFavoriteCharacter(
            favoriteCharacter = favoriteCharacter
        )
    }

    override suspend fun deleteFavouriteCharacter(
        favoriteCharacter: FavouriteCharacterInput
    ) {
        localSource.deleteFavouriteCharacter(
            favoriteCharacter = favoriteCharacter
        )
    }
}