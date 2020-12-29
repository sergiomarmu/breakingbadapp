package com.sermarmu.data.repository

import com.sermarmu.data.entity.Character
import com.sermarmu.data.entity.toCharacter
import com.sermarmu.data.source.network.NetworkSource
import javax.inject.Inject

interface NetworkRepository {
    suspend fun retrieveCharacters(): Set<Character>
}

class NetworkRepositoryImpl @Inject constructor(
    private val networkSource: NetworkSource
) : NetworkRepository {
    override suspend fun retrieveCharacters(): Set<Character> =
        networkSource.retrieveCharacters().toCharacter()
}