package com.sermarmu.data.repository

import com.sermarmu.data.source.local.LocalSource
import com.sermarmu.data.source.local.io.output.FavouriteCharacterOutput
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

private val fakeFavouriteCharacterOutput = FavouriteCharacterOutput(
    charId = 1,
    name = "fakeName",
    isFavourite = true
)

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LocalRepositoryTest {

    @Mock
    private lateinit var localSource: LocalSource

    private lateinit var localRepository: LocalRepository

    @Before
    fun setUp() {
        localRepository = LocalRepositoryImpl(
            localSource
        )
    }

    @Test
    fun retrieveFavouriteCharactersTest() = runBlockingTest {
        `when`(localSource.retrieveFavouriteCharacters())
            .thenReturn(listOf(fakeFavouriteCharacterOutput))

        localRepository
            .retrieveFavouriteCharacters()
            .let {
                assert(it == listOf(fakeFavouriteCharacterOutput))
            }

    }

    @Test
    fun retrieveFavouriteCharacterTest() = runBlockingTest {
        val charId = 1
        `when`(
            localSource.retrieveFavouriteCharacter(
                charId = charId
            )
        ).thenReturn(fakeFavouriteCharacterOutput)

        localRepository
            .retrieveFavouriteCharacter(
                charId = charId
            ).let {
                assert(it == fakeFavouriteCharacterOutput)
            }
    }
}