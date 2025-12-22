package com.example.todo

import com.example.todo.model.service.AccountService
import com.example.todo.model.service.module.AccountModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AccountModule::class]
)
object TestAccountModule {

    @Provides
    @Singleton
    fun provideFakeAccountService(): AccountService = FakeAccountService()
}
