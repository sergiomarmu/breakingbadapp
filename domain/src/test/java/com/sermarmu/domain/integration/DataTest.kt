package com.sermarmu.domain.integration

import com.sermarmu.data.entity.Character
import com.sermarmu.data.repository.NetworkRepository
import com.sermarmu.data.repository.NetworkRepositoryImpl
import com.sermarmu.data.source.network.NetworkSource
import com.sermarmu.data.source.network.io.CharacterOutput
import com.sermarmu.domain.interactor.NetworkInteractor
import com.sermarmu.domain.interactor.NetworkInteractorImpl
import com.sermarmu.domain.model.toCharacterModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

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



@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DataTest {

    // region network
    @Mock
    private lateinit var networkSource: NetworkSource

    private lateinit var networkRepository: NetworkRepository
    private lateinit var networkInteractor: NetworkInteractor
    // endregion network

    @Before
    fun setUp() {
        // region network
        networkRepository = NetworkRepositoryImpl(
            networkSource
        )

        networkInteractor = NetworkInteractorImpl(
            networkRepository
        )
        // endregion network
    }

    @Test
    fun retrieveCharactersNetworkTest() = runBlockingTest {
        `when`(networkSource.retrieveCharacters())
            .thenReturn(listOf(fakeCharacterOutput))

        networkInteractor
            .retrieveCharacters()
            .first()
            .let {
                assert(it == NetworkInteractor.CharacterState.Success(setOf(fakeCharacter).toCharacterModel()))
            }
    }
}