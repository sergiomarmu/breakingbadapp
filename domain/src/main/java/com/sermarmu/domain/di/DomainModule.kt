package com.sermarmu.domain.di

import com.sermarmu.domain.interactor.NetworkInteractor
import com.sermarmu.domain.interactor.NetworkInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DomainModule {

    // region interactor
    @Singleton
    @Provides
    fun provideInteractor(
        networkInteractor: NetworkInteractorImpl
    ): NetworkInteractor = networkInteractor
    // endregion interactor
}