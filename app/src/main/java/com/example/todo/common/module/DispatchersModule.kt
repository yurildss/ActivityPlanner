package com.example.todo.common.module

import com.example.todo.common.AppDispatchers
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    @Provides
    fun provideDispatcher() : AppDispatchers   {
        return AppDispatchers(
            main = Dispatchers.Main,
            io = Dispatchers.IO
        )
    }
}