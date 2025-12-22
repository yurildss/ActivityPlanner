package com.example.todo.model.service.module

import com.example.todo.model.service.AccountService
import com.example.todo.model.service.impl.AccountServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AccountModule {
    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService
}