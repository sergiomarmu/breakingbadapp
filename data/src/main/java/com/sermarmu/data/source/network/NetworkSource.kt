package com.sermarmu.data.source.network

import com.sermarmu.data.handler.tryRequest
import com.sermarmu.data.source.network.io.CharacterOutput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface NetworkSource {
    suspend fun retrieveCharacters(): List<CharacterOutput>
}

class NetworkSourceImpl @Inject constructor(
    private val networkApi: NetworkApi
) : NetworkSource {
    override suspend fun retrieveCharacters(): List<CharacterOutput> =
        tryRequest {
            withContext(Dispatchers.IO) {
                networkApi.retrieveCharacters()
            }
        }
}