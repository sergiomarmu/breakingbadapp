package com.sermarmu.data.di

import com.sermarmu.data.BuildConfig
import com.sermarmu.data.repository.NetworkRepository
import com.sermarmu.data.repository.NetworkRepositoryImpl
import com.sermarmu.data.source.network.NetworkApi
import com.sermarmu.data.source.network.NetworkSource
import com.sermarmu.data.source.network.NetworkSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DataModule {

    // region retrofit
    @Singleton
    @Provides
    @Named("breakingbad_okhttpclient")
    fun provideOkkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .setLevel(
                    if (BuildConfig.DEBUG)
                        HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE
                )
        ).connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    @Named("breakingbad_retrofit")
    fun provideRetrofit(
        @Named("breakingbad_okhttpclient")
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(
            GsonConverterFactory.create()
        )
        .baseUrl("https://www.breakingbadapi.com/api/")
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideNetworkApi(
        @Named("breakingbad_retrofit") retrofit: Retrofit
    ): NetworkApi = retrofit.create(NetworkApi::class.java)
    // endregion retrofit

    // region source
    @Singleton
    @Provides
    fun provideNetworkSource(
        networkSource: NetworkSourceImpl
    ): NetworkSource = networkSource
    // endregion source

    // region repository
    @Singleton
    @Provides
    fun provideNetworkRepository(
        networkRepository: NetworkRepositoryImpl
    ): NetworkRepository = networkRepository
    // endregion repository
}