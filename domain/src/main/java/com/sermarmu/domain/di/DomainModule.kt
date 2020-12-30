package com.sermarmu.domain.di

import com.sermarmu.domain.interactor.LocalInteractor
import com.sermarmu.domain.interactor.LocalInteractorImpl
import com.sermarmu.domain.interactor.NetworkInteractor
import com.sermarmu.domain.interactor.NetworkInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(ApplicationComponent::class)
object DomainModule {

    // region interactor
    @Singleton
    @Provides
    fun provideNetworkInteractor(
        networkInteractor: NetworkInteractorImpl
    ): NetworkInteractor = networkInteractor

    @Singleton
    @Provides
    fun provideLocalInteractor(
        localInteractor: LocalInteractorImpl
    ): LocalInteractor = localInteractor
    // endregion interactor
}