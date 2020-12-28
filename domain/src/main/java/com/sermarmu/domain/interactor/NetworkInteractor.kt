package com.sermarmu.domain.interactor

import com.sermarmu.data.repository.NetworkRepository
import com.sermarmu.domain.model.CharacterModel
import com.sermarmu.domain.model.toCharacterModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface NetworkInteractor {
    suspend fun retrieveCharacters(): Flow<Set<CharacterModel>>
}

class NetworkInteractorImpl @Inject constructor(
    private val repository: NetworkRepository
) : NetworkInteractor {
    override suspend fun retrieveCharacters(): Flow<Set<CharacterModel>> =
        flow {
            emit(repository.retrieveCharacters().toCharacterModel())
        }
}