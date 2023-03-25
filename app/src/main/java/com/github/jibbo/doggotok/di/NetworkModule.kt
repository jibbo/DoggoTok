package com.github.jibbo.doggotok.di

import com.github.jibbo.doggotok.api.DogsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun providesDogApiService(): DogsApiService = DogsApiService.create()
}