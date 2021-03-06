package com.sermarmu.domain.model

import com.sermarmu.data.entity.FavouriteCharacter
import com.sermarmu.data.source.local.io.output.FavouriteCharacterOutput
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toSet

data class FavouriteCharacterModel(
    val charId: Int,
    val name: String,
    var isFavourite: Boolean = false
)

suspend fun Iterable<FavouriteCharacter>.toFavouriteCharactersModel() = asFlow().map {
    it.toFavouriteCharacterModel()
}.toSet()

fun FavouriteCharacter.toFavouriteCharacterModel() =
    FavouriteCharacterModel(
        charId = this.charId,
        name = this.name,
        isFavourite = this.isFavourite
    )