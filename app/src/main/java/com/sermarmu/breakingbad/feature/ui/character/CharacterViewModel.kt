package com.sermarmu.breakingbad.feature.ui.character

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sermarmu.domain.interactor.LocalInteractor
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
    private val networkInteractor: NetworkInteractor,
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
    private val userRefreshActionMutableStateFlow = MutableStateFlow(0)
    private val userAddFavouriteCharacterMutableStateFlow = MutableStateFlow(0)
    // endregion mutableStateFlow

    // region livedata
    private var characterStateJob: Job? = null
    internal val characterStateLiveData = object : LiveData<CharacterState>() {
        override fun onActive() {
            super.onActive()
            val oldJob = characterStateJob
            characterStateJob = viewModelScope.launch {
                oldJob?.cancel()

                networkInteractor.retrieveCharactersFlow(
                    userRefreshActionMutableStateFlow = userRefreshActionMutableStateFlow,
                    userAddFavouriteCharacterMutableStateFlow = userAddFavouriteCharacterMutableStateFlow
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

    // region userAction
    private var userRefreshJob: Job? = null
    internal fun onUserRefreshAction() {
        val oldJob = userRefreshJob
        userRefreshJob = viewModelScope.launch {
            oldJob?.cancel()
            val userRefreshActionValue = userRefreshActionMutableStateFlow.value
            userRefreshActionMutableStateFlow.value = userRefreshActionValue.inc()
        }
    }

    private var userAddFavouriteCharacterJob: Job? = null
    internal fun onUserAddFavouriteCharacterAction(
        characterModel: CharacterModel
    ) {
        val oldJob = userAddFavouriteCharacterJob
        userAddFavouriteCharacterJob = viewModelScope.launch {
            oldJob?.cancel()
            localInteractor.addFavouriteCharacter(
                characterModel
            )
            val userAddActionValue = userAddFavouriteCharacterMutableStateFlow.value
            userAddFavouriteCharacterMutableStateFlow.value = userAddActionValue.inc()
        }
    }
    // endregion userAction

}