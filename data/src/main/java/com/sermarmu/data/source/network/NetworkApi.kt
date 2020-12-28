package com.sermarmu.data.source.network

import com.sermarmu.data.source.network.io.CharacterOutput
import retrofit2.Response
import retrofit2.http.GET

interface NetworkApi {
    // region character
    @GET("characters")
    suspend fun retrieveCharacters(): Response<List<CharacterOutput>>
    // endregion character
}