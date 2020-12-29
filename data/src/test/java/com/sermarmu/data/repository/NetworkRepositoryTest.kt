package com.sermarmu.data.repository

import com.sermarmu.data.source.network.NetworkSource
import com.sermarmu.data.source.network.io.CharacterOutput
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.sermarmu.data.entity.Character
import com.sermarmu.data.entity.toCharacter
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

private val fakeCharacter = Character(
    charId = 1,
    name = "fakeName",
    birthday = "fakeBirthDay",
    occupation = listOf("fakeOccupation"),
    img = "fakeImg",
    status = Character.Status.ALIVE,
    appearance = listOf(3),
    nickname = "fakeNickname",
    portrayed = "fakePortrayed"
)

private val fakeCharacterOutput = CharacterOutput(
    charId = 1,
    name = "fakeName",
    birthday = "fakeBirthDay",
    occupation = listOf("fakeOccupation"),
    img = "fakeImg",
    status = CharacterOutput.Status.ALIVE,
    appearance = listOf(3),
    nickname = "fakeNickname",
    portrayed = "fakePortrayed"
)

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NetworkRepositoryTest {

    @Mock
    private lateinit var networkSource: NetworkSource

    private lateinit var networkRepository: NetworkRepository

    @Before
    fun setUp() {
        networkRepository = NetworkRepositoryImpl(
            networkSource
        )
    }

    @Test
    fun retrieveCharactersOutputToCharacterTest() = runBlockingTest {

        val fakeCharacterList = setOf(fakeCharacter)
        val fakeCharacterConvertedList = setOf(fakeCharacterOutput).toCharacter()

        assert(fakeCharacterList == fakeCharacterConvertedList)
    }

    @Test
    fun retrieveCharactersTest() = runBlockingTest {
        Mockito.`when`(networkSource.retrieveCharacters())
            .thenReturn(listOf(fakeCharacterOutput))

        val result =
            networkRepository
                .retrieveCharacters()

        assert(result == setOf(fakeCharacter))
    }
}