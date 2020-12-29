package com.sermarmu.domain.model

import com.sermarmu.data.source.local.io.output.FavouriteCharacterOutput
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toSet

data class FavouriteCharacterModel(
    val charId: Int,
    val name: String,
    var isFavourite: Boolean = false
)

suspend fun Iterable<FavouriteCharacterOutput>.toFavouriteCharacterModel() = asFlow().map {
    FavouriteCharacterModel(
        charId = it.charId,
        name = it.name,
        isFavourite = it.isFavourite
    )
}.toSet()