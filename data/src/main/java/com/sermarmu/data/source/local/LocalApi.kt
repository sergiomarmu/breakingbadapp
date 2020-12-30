package com.sermarmu.data.source.local

import androidx.room.*
import com.sermarmu.data.source.local.io.input.FavouriteCharacterInput
import com.sermarmu.data.source.local.io.output.FavouriteCharacterOutput

@Dao
interface LocalApi {
    @Query("SELECT * FROM favouritecharacterinput")
    suspend fun retrieveFavoriteCharacters() : List<FavouriteCharacterOutput>

    @Query("SELECT * FROM favouritecharacterinput WHERE charId LIKE :charId LIMIT 1")
    suspend fun retrieveFavoriteCharacter(
        charId: Int
    ) : FavouriteCharacterOutput?

    @Insert
    suspend fun insertFavouriteCharacters(
        vararg favoriteCharacter: FavouriteCharacterInput
    )

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateFavoriteCharacter(
        favoriteCharacter: FavouriteCharacterInput
    )

    @Delete
    suspend fun deleteFavouriteCharacter(
        favoriteCharacter: FavouriteCharacterInput
    )
}