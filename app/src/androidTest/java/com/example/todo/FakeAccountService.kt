package com.example.todo

import com.example.todo.model.User
import com.example.todo.model.service.AccountService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow

class FakeAccountService(
) : AccountService {

    private val _currentUser = MutableStateFlow(
        User(
            id = "1",
            name = "Regina Phalangee",
            email = "test@android.com"
        )
    )

    override val currentUserId: String
        get() {
            return "1"
        }
    override val hasUser: Boolean
        get() {
            return true
        }
    override val currentUser: Flow<User>
        get() = _currentUser


    override suspend fun register(
        email: String,
        password: String,
        name: String
    ) {
        Result.success(Unit)
    }

    override suspend fun createAnonymousAccount() {
        TODO("Not yet implemented")
    }

    override suspend fun authenticate(email: String, password: String) {
        Result.success(Unit)
    }

    override suspend fun linkAccount(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updatePassword(
        email: String,
        password: String,
        oldPassword: String
    ) {
        Result.success(Unit)
    }

    override suspend fun updateName(name: String) {
        currentUser.collect {
            it.copy(name = name)
        }
    }

    override suspend fun sendRecoveryEmail(email: String) {
        Result.success(Unit)
    }
}