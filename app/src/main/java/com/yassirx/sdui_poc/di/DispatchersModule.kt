package com.harbin.vtcdrivertransport.di

import com.yassirx.sdui_poc.data.network.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DispatchersModule {

    @Singleton
    @Provides
    fun provideUserApi(
         retrofit: Retrofit,
    ): UserApi {
        return retrofit.create(UserApi::class.java)
    }

}