package com.example.todo.model.service.module

import com.example.todo.model.service.LogService
import com.example.todo.model.service.impl.LogServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LogModule{
    @Binds abstract fun provideLogService(impl: LogServiceImpl): LogService
}