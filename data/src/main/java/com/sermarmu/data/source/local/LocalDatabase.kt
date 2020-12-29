package com.sermarmu.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sermarmu.data.source.local.io.input.FavouriteCharacterInput
import com.sermarmu.data.source.local.io.output.FavouriteCharacterOutput


@Database(
    entities = [
        FavouriteCharacterInput::class,
        FavouriteCharacterOutput::class
    ], version = 1
)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun localApi(): LocalApi
}