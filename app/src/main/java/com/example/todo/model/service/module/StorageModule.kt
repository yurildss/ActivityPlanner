package com.example.todo.model.service.module

import com.example.todo.model.service.StorageService
import com.example.todo.model.service.impl.StorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {

    @Binds abstract fun provideStorageService(impl: StorageServiceImpl): StorageService

}

