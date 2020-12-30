package com.sermarmu.domain.interactor

import com.sermarmu.data.entity.Character
import com.sermarmu.data.repository.NetworkRepository
import com.sermarmu.data.source.local.io.output.FavouriteCharacterOutput
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
import org.mockito.Mockito.`when`
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

private val fakeFavouriteCharacterOutput = FavouriteCharacterOutput(
    charId = 1,
    name = "fakeName",
    isFavourite = true
)

private val fakeCharacterModel = CharacterModel(
    charId = 1,
    name = "fakeName",
    birthday = "fakeBirthDay",
    occupation = listOf("fakeOccupation"),
    img = "fakeImg",
    status = CharacterModel.Status.ALIVE,
    appearance = listOf(3),
    nickname = "fakeNickname",
    portrayed = "fakePortrayed"
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
class NetworkInteractorTest {

    @Mock
    private lateinit var networkRepository: NetworkRepository

    @Mock
    private lateinit var localInteractor: LocalInteractor

    private lateinit var networkInteractor: NetworkInteractor

    private val userAction = MutableStateFlow(0)

    @Before
    fun setUp() {
        networkInteractor = NetworkInteractorImpl(
            networkRepository,
            localInteractor
        )
    }

    @Test
    fun retrieveCharactersToCharactersModelTest() = runBlockingTest {

        val fakeCharacterList = setOf(fakeCharacterModel)
        val fakeCharacterModelConvertedList = setOf(fakeCharacter).toCharacterModel()

        assert(fakeCharacterList == fakeCharacterModelConvertedList)
    }

    @Test
    fun retrieveCharactersTest() = runBlockingTest {
        `when`(networkRepository.retrieveCharacters())
            .thenReturn(setOf(fakeCharacter))
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
                assert(
                    state.characters.toMutableSet().first {
                        it.charId == 1
                    } == setOf(fakeCharacterModelSuccess).toMutableSet().first()
                )
            }
    }
}
