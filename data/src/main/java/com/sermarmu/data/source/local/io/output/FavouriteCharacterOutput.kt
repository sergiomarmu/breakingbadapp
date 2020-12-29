package com.sermarmu.data.source.local.io.output

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class FavouriteCharacterOutput(
    @PrimaryKey val charId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "isFavourite") val isFavourite: Boolean
)