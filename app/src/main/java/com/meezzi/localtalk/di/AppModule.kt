package com.meezzi.localtalk.di

import android.content.Context
import com.meezzi.localtalk.repository.AuthRepository
import com.meezzi.localtalk.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAuthRepository(
        @ApplicationContext context: Context
    ): AuthRepository {
        return AuthRepository(context)
    }

    @Singleton
    @Provides
    fun provideHomeRepository(
        @ApplicationContext context: Context
    ): HomeRepository {
        return HomeRepository(context)
    }
}