package com.uwange.coffeeapp.data.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.uwange.coffeeapp.data.repository.CouponPointRepository
import com.uwange.coffeeapp.data.repository.ImageRepository
import com.uwange.coffeeapp.data.repository.UserRepository
import com.uwange.coffeeapp.data.repositoryImpl.CouponPointRepositoryImpl
import com.uwange.coffeeapp.data.repositoryImpl.ImageRepositoryImpl
import com.uwange.coffeeapp.data.repositoryImpl.UserRepositoryImpl
import com.uwange.coffeeapp.sharedpreference.CoffeePreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideCoffeePreference(@ApplicationContext context: Context) : SharedPreferences =
        context.getSharedPreferences(context.packageName, MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideImageRepository(): ImageRepository =
        ImageRepositoryImpl()

    @Provides
    @Singleton
    fun provideUserRepository(pref: CoffeePreference): UserRepository =
        UserRepositoryImpl(pref)

    @Provides
    @Singleton
    fun provideCouponPointRepository(pref: CoffeePreference): CouponPointRepository =
        CouponPointRepositoryImpl(pref)
}