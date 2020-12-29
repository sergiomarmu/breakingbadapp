package com.sermarmu.data.di

import android.content.Context
import androidx.room.Room
import com.sermarmu.data.BuildConfig
import com.sermarmu.data.repository.LocalRepository
import com.sermarmu.data.repository.LocalRepositoryImpl
import com.sermarmu.data.repository.NetworkRepository
import com.sermarmu.data.repository.NetworkRepositoryImpl
import com.sermarmu.data.source.local.LocalApi
import com.sermarmu.data.source.local.LocalDatabase
import com.sermarmu.data.source.local.LocalSource
import com.sermarmu.data.source.local.LocalSourceImpl
import com.sermarmu.data.source.network.NetworkApi
import com.sermarmu.data.source.network.NetworkSource
import com.sermarmu.data.source.network.NetworkSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
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

    // region network
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
    // endregion network

    // region local
    // region room
    @Singleton
    @Provides
    fun providelocalDatabase(
        localDatabase: LocalDatabase
    ): LocalApi = localDatabase.localApi()

    @Singleton
    @Provides
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        LocalDatabase::class.java,
        "breakingbad-database"
    ).build()
    // endregion room

    // region source
    @Singleton
    @Provides
    fun provideLocalSource(
        localSource: LocalSourceImpl
    ): LocalSource = localSource
    // endregion source

    // region repository
    @Singleton
    @Provides
    fun provideLocalRepository(
        localRepository: LocalRepositoryImpl
    ): LocalRepository = localRepository
    // endregion repository
    // endregion local
}