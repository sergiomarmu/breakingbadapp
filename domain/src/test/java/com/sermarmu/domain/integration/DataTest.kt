package com.sermarmu.domain.integration

import com.sermarmu.data.entity.Character
import com.sermarmu.data.repository.LocalRepository
import com.sermarmu.data.repository.NetworkRepository
import com.sermarmu.data.repository.NetworkRepositoryImpl
import com.sermarmu.data.source.local.io.output.FavouriteCharacterOutput
import com.sermarmu.data.source.network.NetworkSource
import com.sermarmu.data.source.network.io.CharacterOutput
import com.sermarmu.domain.interactor.*
import com.sermarmu.domain.model.CharacterModel
import com.sermarmu.domain.model.toCharacterModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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

private val fakeFavouriteCharacterOutput = FavouriteCharacterOutput(
    charId = 1,
    name = "fakeName",
    isFavourite = true
)

private val fakeCharacterModelSuccess = CharacterModel(
    charId = 1,
    name = "fakeName",
    birthday = "fakeBirthDay",
    occupation = listOf("fakeOccupation"),
    img = "fakeImg",
    status = CharacterModel.Status.ALIVE,
    appearance = listOf(3),
    nickname = "fakeNickname",
    portrayed = "fakePortrayed",
    isFavourite = true
)

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DataTest {

    // region network
    @Mock
    private lateinit var networkSource: NetworkSource
    @Mock
    private lateinit var localInteractor: LocalInteractor

    private lateinit var networkRepository: NetworkRepository
    private lateinit var networkInteractor: NetworkInteractor

    private val userAction = MutableStateFlow(0)
    // endregion network

    @Before
    fun setUp() {
        // region network
        networkRepository = NetworkRepositoryImpl(
            networkSource
        )

        networkInteractor = NetworkInteractorImpl(
            networkRepository,
            localInteractor
        )
        // endregion network
    }

    @Test
    fun retrieveCharactersTest() = runBlockingTest {
        `when`(networkSource.retrieveCharacters())
            .thenReturn(listOf(fakeCharacterOutput))

        `when`(localInteractor.retrieveFavouriteCharactersFlow())
            .thenReturn(flowOf(listOf(fakeFavouriteCharacterOutput)))

        networkInteractor
            .retrieveCharactersFlow(
                userActionMutableStateFlow = userAction
            )
            .first()
            .let { state ->
                assert(state is NetworkInteractor.CharacterState.Success)
                assert(
                    (state as NetworkInteractor.CharacterState.Success)
                        .characters.size
                            == setOf(fakeCharacterModelSuccess).size
                )
                state.characters.toMutableSet().first {
                    it.charId == 1
                } == setOf(fakeCharacterModelSuccess).toMutableSet().first()
            }
    }
}