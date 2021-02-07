package com.sermarmu.breakingbad.feature.ui.character

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sermarmu.domain.interactor.CharacterInteractor
import com.sermarmu.domain.interactor.LocalInteractor
import com.sermarmu.domain.interactor.NetworkInteractor
import com.sermarmu.domain.model.CharacterModel
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
@FragmentScoped
class CharacterViewModel @ViewModelInject constructor(
    private val characterInteractor: CharacterInteractor,
    private val localInteractor: LocalInteractor
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
    // see https://developer.android.com/kotlin/flow/stateflow-and-sharedflow
    private val userActionMutableStateFlow = MutableStateFlow(0)
    // endregion mutableStateFlow

    // region livedata
    private var characterStateJob: Job? = null
    internal val characterStateLiveData = object : LiveData<CharacterState>() {
        override fun onActive() {
            super.onActive()
            val oldJob = characterStateJob
            characterStateJob = viewModelScope.launch {
                oldJob?.cancel()

                characterInteractor.retrieveCharactersFlow(
                    userActionMutableStateFlow = userActionMutableStateFlow
                ).mapLatest {
                    when (it) {
                        is CharacterInteractor.CharacterState.Success ->
                            CharacterState.Success(it.characters)
                        is CharacterInteractor.CharacterState.Failure.NoInternet ->
                            CharacterState.Failure.NoInternet(it.message)
                        is CharacterInteractor.CharacterState.Failure.Unexpected ->
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

    // region userAction
    private var userRefreshJob: Job? = null
    internal fun onUserRefreshAction() {
        val oldJob = userRefreshJob
        userRefreshJob = viewModelScope.launch {
            oldJob?.cancel()
            val userRefreshActionValue = userActionMutableStateFlow.value.inc()
            userActionMutableStateFlow.value = userRefreshActionValue
        }
    }

    private var userAddFavouriteCharacterJob: Job? = null
    internal fun onUserAddFavouriteCharacterAction(
        characterModel: CharacterModel
    ) {
        val oldJob = userAddFavouriteCharacterJob
        userAddFavouriteCharacterJob = viewModelScope.launch {
            oldJob?.cancel()
            characterInteractor.addFavouriteCharacter(
                characterModel
            )
            val userAddActionValue = userActionMutableStateFlow.value.inc()
            userActionMutableStateFlow.value = userAddActionValue
        }
    }
    // endregion userAction
}