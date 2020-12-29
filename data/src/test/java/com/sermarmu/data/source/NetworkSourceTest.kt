package com.sermarmu.data.source

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sermarmu.data.source.network.NetworkApi
import com.sermarmu.data.source.network.NetworkSource
import com.sermarmu.data.source.network.NetworkSourceImpl
import com.sermarmu.data.source.network.io.CharacterOutput
import com.sermarmu.data.utils.MockJson
import com.sermarmu.data.utils.retrieveApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val fakeCharacterJsonPath = "characters_network_response.json"
private const val fakePortServer = 8080
private const val fakeUrl = "/"

@RunWith(MockitoJUnitRunner::class)
class NetworkSourceTest {

    private val fakeServer = MockWebServer()


    @Mock
    private lateinit var networkApi: NetworkApi

    private lateinit var networkSource: NetworkSource
    private lateinit var gson: Gson
    private lateinit var mockJson: MockJson

    private lateinit var fakeCharacterOutput: List<CharacterOutput>

    @Before
    fun setUp() {
        // Init server
        fakeServer.start(fakePortServer)

        // Init Api & necessary elements
        networkApi = fakeServer.retrieveApi(fakeUrl)

        networkSource = NetworkSourceImpl(networkApi)
        gson = GsonBuilder().create()
        mockJson = MockJson()

        fakeCharacterOutput = gson.fromJson(
            mockJson.json(fakeCharacterJsonPath),
            Array<CharacterOutput>::class.java
        ).toList()
    }

    @Test
    fun retrieveCharactersTest() = runBlocking {
        fakeServer.enqueue(
            MockResponse().setBody(
                mockJson.json(fakeCharacterJsonPath)
            )
        )

        networkSource
            .retrieveCharacters()
            .first()
            .let {
                assert(it == fakeCharacterOutput[0])
            }
    }

    @After
    fun closeServer() {
        // Close server
        fakeServer.close()
    }
}