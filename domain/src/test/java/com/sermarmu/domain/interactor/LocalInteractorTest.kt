package com.sermarmu.domain.interactor

import com.sermarmu.data.entity.FavouriteCharacter
import com.sermarmu.data.repository.LocalRepository
import com.sermarmu.domain.model.toFavouriteCharacterModel
import com.sermarmu.domain.model.toFavouriteCharactersModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

private val fakeFavouriteCharacters = FavouriteCharacter(
    charId = 1,
    name = "fakeName",
    isFavourite = true
)

private val fakeFavouriteCharacter = FavouriteCharacter(
    charId = 2,
    name = "fakeName",
    isFavourite = true
)

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LocalInteractorTest {

    @Mock
    private lateinit var localRepository: LocalRepository

    private lateinit var localInteractor: LocalInteractor

    @Before
    fun setUp() {
        localInteractor = LocalInteractorImpl(
            localRepository
        )
    }

    @Test
    fun retrieveFavouriteCharacterOutputTest() = runBlockingTest {
        `when`(localRepository.retrieveFavouriteCharacters())
            .thenReturn(setOf(fakeFavouriteCharacters))

        localInteractor
            .retrieveFavouriteCharactersFlow()
            .first()
            .let {
                assert(it == listOf(fakeFavouriteCharacters).toFavouriteCharactersModel())
            }
    }

    @Test
    fun retrieveCharactersTest() = runBlockingTest {
        val fakeCharId = 2
        `when`(
            localRepository.retrieveFavouriteCharacter(
                fakeCharId
            )
        ).thenReturn(fakeFavouriteCharacter)

        localInteractor
            .retrieveFavouriteCharacterFlow(
                fakeCharId
            ).first()
            .let {
                assert(it != null)
                assert(it == fakeFavouriteCharacter.toFavouriteCharacterModel())
            }
    }
}
