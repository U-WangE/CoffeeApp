package com.uwange.coffeeapp.data.di

import com.uwange.coffeeapp.data.repository.ImageRepository
import com.uwange.coffeeapp.data.repositoryImpl.ImageRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideImageRepository(): ImageRepository =
        ImageRepositoryImpl()
}