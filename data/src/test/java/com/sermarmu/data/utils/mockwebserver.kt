package com.sermarmu.data.utils

import com.sermarmu.data.source.network.NetworkApi
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun MockWebServer.retrieveApi(
    fakeUrl: String
): NetworkApi = Retrofit
    .Builder()
    .baseUrl(this.url(fakeUrl))
    .addConverterFactory(
        GsonConverterFactory
            .create()
    ).build()
    .create(NetworkApi::class.java)