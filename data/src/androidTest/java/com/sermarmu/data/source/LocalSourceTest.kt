package com.sermarmu.data.source

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sermarmu.data.source.local.LocalApi
import com.sermarmu.data.source.local.LocalDatabase
import com.sermarmu.data.source.local.LocalSource
import com.sermarmu.data.source.local.LocalSourceImpl
import com.sermarmu.data.source.local.io.input.FavouriteCharacterInput
import com.sermarmu.data.source.local.io.output.FavouriteCharacterOutput
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private val fakeFavouriteCharacterInput = FavouriteCharacterInput(
    charId = 1,
    name = "fakeName",
    isFavourite = true
)

private val fakeFavouriteCharacterOutput = FavouriteCharacterOutput(
    charId = 1,
    name = "fakeName",
    isFavourite = true
)

private val fakeFavouriteCharacterUpdatedInput = FavouriteCharacterInput(
    charId = 1,
    name = "fakeNameUpdated",
    isFavourite = false
)

private val fakeFavouriteCharacterUpdatedOutput = FavouriteCharacterOutput(
    charId = 1,
    name = "fakeNameUpdated",
    isFavourite = false
)

@RunWith(AndroidJUnit4::class)
class LocalSourceTest {

    private lateinit var localApiDao: LocalApi
    private lateinit var localDatabase: LocalDatabase

    private lateinit var localSource: LocalSource

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        localDatabase = Room.inMemoryDatabaseBuilder(
            context, LocalDatabase::class.java
        ).build()
        localApiDao = localDatabase.localApi()

        localSource = LocalSourceImpl(
            localApiDao
        )
    }

    @Test
    fun retrieveFavouriteCharactersTest() = runBlocking {
        localSource.insertFavouriteCharacters(fakeFavouriteCharacterInput)

        localSource
            .retrieveFavouriteCharacters()
            .first()
            .let {
                Assert.assertTrue(
                    it.charId == fakeFavouriteCharacterOutput.charId
                )
                Assert.assertTrue(
                    it.name == fakeFavouriteCharacterOutput.name
                )
                Assert.assertTrue(
                    it.isFavourite == fakeFavouriteCharacterOutput.isFavourite
                )
            }
    }

    @Test
    fun retrieveFavouriteCharacterTest() = runBlocking {
        val fakeCharId = 1
        localSource.insertFavouriteCharacters(fakeFavouriteCharacterInput)

        localSource
            .retrieveFavouriteCharacter(
                fakeCharId
            ).let {
                Assert.assertNotNull(it)
                Assert.assertEquals(
                    it!!.charId,
                    fakeFavouriteCharacterOutput.charId
                )
                Assert.assertEquals(
                    it.name,
                    fakeFavouriteCharacterOutput.name
                )
                Assert.assertEquals(
                    it.isFavourite,
                    fakeFavouriteCharacterOutput.isFavourite
                )
            }
    }

    @Test
    fun updateFavouriteCharacterTest() = runBlocking {
        val fakeCharId = 1
        localSource.insertFavouriteCharacters(fakeFavouriteCharacterInput)

        localSource
            .updateFavoriteCharacter(fakeFavouriteCharacterUpdatedInput)

        localSource
            .retrieveFavouriteCharacter(
                fakeCharId
            ).let {
                Assert.assertNotNull(it)
                Assert.assertEquals(
                    it!!.charId,
                    fakeFavouriteCharacterUpdatedOutput.charId
                )
                Assert.assertEquals(
                    it.name,
                    fakeFavouriteCharacterUpdatedOutput.name
                )
                Assert.assertEquals(
                    it.isFavourite,
                    fakeFavouriteCharacterUpdatedOutput.isFavourite
                )
            }
    }

    @Test
    fun deleteCharacterTest() = runBlocking {
        val fakeCharId = 1
        localSource.insertFavouriteCharacters(fakeFavouriteCharacterInput)

        localSource
            .deleteFavouriteCharacter(fakeFavouriteCharacterUpdatedInput)

        localSource
            .retrieveFavouriteCharacter(
                fakeCharId
            ).let {
                Assert.assertNull(it)
            }
    }

    @After
    fun closeDatabase() {
        localDatabase.close()
    }
}