package com.sermarmu.data.source.local

import com.sermarmu.data.source.local.io.input.FavouriteCharacterInput
import com.sermarmu.data.source.local.io.output.FavouriteCharacterOutput
import javax.inject.Inject

interface LocalSource {
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

class LocalSourceImpl @Inject constructor(
    private val localApi: LocalApi
) : LocalSource {
    override suspend fun retrieveFavouriteCharacters(): List<FavouriteCharacterOutput> =
        localApi.retrieveFavoriteCharacters()


    override suspend fun retrieveFavouriteCharacter(
        charId: Int
    ): FavouriteCharacterOutput? = localApi.retrieveFavoriteCharacter(
            charId = charId
        )

    override suspend fun insertFavouriteCharacters(
        vararg favoriteCharacter: FavouriteCharacterInput
    ) {
        localApi.insertFavouriteCharacters(
            favoriteCharacter = *favoriteCharacter
        )
    }

    override suspend fun updateFavoriteCharacter(
        favoriteCharacter: FavouriteCharacterInput
    ) {
        localApi.updateFavoriteCharacter(
            favoriteCharacter = favoriteCharacter
        )
    }

    override suspend fun deleteFavouriteCharacter(
        favoriteCharacter: FavouriteCharacterInput
    ) {
        localApi.deleteFavouriteCharacter(
            favoriteCharacter = favoriteCharacter
        )
    }
}