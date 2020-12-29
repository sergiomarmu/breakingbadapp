package com.sermarmu.breakingbad.feature.ui.character

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sermarmu.domain.interactor.NetworkInteractor
import com.sermarmu.domain.model.CharacterModel
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapLatest

@ExperimentalCoroutinesApi
@FragmentScoped
class CharacterViewModel @ViewModelInject constructor(
    private val networkInteractor: NetworkInteractor
) : ViewModel() {

    // region state
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
    // endregion state

    // region mutableStateFlow
    private val userRefreshActionMutableStateFlow = MutableStateFlow(0)
    // endregion mutableStateFlow

    // region livedata
    private var characterStateJob: Job? = null
    internal val characterStateLiveData = object : LiveData<CharacterState>() {
        override fun onActive() {
            super.onActive()
            val oldJob = characterStateJob
            characterStateJob = viewModelScope.launch {
                oldJob?.cancel()

                networkInteractor.retrieveCharacters(
                    userRefreshActionMutableStateFlow = userRefreshActionMutableStateFlow
                ).mapLatest {
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
    // endregion livedata

    private var userRefreshJob: Job? = null
    internal fun onUserRefreshAction() {
        val oldJob = userRefreshJob
        userRefreshJob = viewModelScope.launch {
            oldJob?.cancel()
            val userRefreshActionValue = userRefreshActionMutableStateFlow.value
            userRefreshActionMutableStateFlow.value = userRefreshActionValue.inc()
        }
    }
}