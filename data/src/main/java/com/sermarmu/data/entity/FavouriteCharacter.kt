package com.sermarmu.data.entity

import com.sermarmu.data.source.local.io.output.FavouriteCharacterOutput
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toSet

data class FavouriteCharacter(
    val charId: Int,
    val name: String,
    var isFavourite: Boolean = false
)

suspend fun Iterable<FavouriteCharacterOutput>.toFavouriteCharacters() = asFlow().map {
    it.toFavouriteCharacter()
}.toSet()

fun FavouriteCharacterOutput.toFavouriteCharacter() =
    FavouriteCharacter(
        charId = this.charId,
        name = this.name,
        isFavourite = this.isFavourite
    )