package com.sermarmu.domain.interactor

import com.sermarmu.data.repository.NetworkRepository
import com.sermarmu.domain.model.CharacterModel
import com.sermarmu.domain.model.toCharactersModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
interface NetworkInteractor {
    suspend fun retrieveCharacters(): Flow<Set<CharacterModel>>
}

@ExperimentalCoroutinesApi
class NetworkInteractorImpl @Inject constructor(
    private val networkRepository: NetworkRepository
) : NetworkInteractor {
    override suspend fun retrieveCharacters(): Flow<Set<CharacterModel>> =
        flow {
            emit(
                networkRepository.retrieveCharacters().toCharactersModel()
            )
        }
}