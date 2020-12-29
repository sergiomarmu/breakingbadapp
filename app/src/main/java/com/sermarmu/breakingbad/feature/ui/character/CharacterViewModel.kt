package com.sermarmu.breakingbad.feature.ui.character

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sermarmu.domain.interactor.NetworkInteractor
import com.sermarmu.domain.model.CharacterModel
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapLatest

@ExperimentalCoroutinesApi
@FragmentScoped
class CharacterViewModel @ViewModelInject constructor(
    private val networkInteractor: NetworkInteractor
) : ViewModel() {
    internal sealed class CharacterState {
        data class Success(
            val characters: Set<CharacterModel>
        ) : CharacterState()

        sealed class Failure : CharacterState() {
            data class NoInternet(
                val message: String? = null
            ) : Failure()

            data class Unexpected(
                val message: String? = null
            ) : Failure()
        }
    }

    private var characterStateJob: Job? = null
    internal val characterStateLiveData = object : LiveData<CharacterState>() {
        override fun onActive() {
            super.onActive()
            val oldJob = characterStateJob
            characterStateJob = viewModelScope.launch {
                oldJob?.cancel()

                networkInteractor.retrieveCharacters()
                    .mapLatest {
                        when (it) {
                            is NetworkInteractor.CharacterState.Success ->
                                CharacterState.Success(it.characters)
                            is NetworkInteractor.CharacterState.Failure.NoInternet ->
                                CharacterState.Failure.NoInternet(it.message)
                            is NetworkInteractor.CharacterState.Failure.Unexpected ->
                                CharacterState.Failure.Unexpected(it.message)
                        }
                    }.collect {
                        withContext(Dispatchers.Main) {
                            value = it
                        }
                    }
            }
        }
    }
}