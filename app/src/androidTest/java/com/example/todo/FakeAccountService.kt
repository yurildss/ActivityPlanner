package com.example.todo

import com.example.todo.model.User
import com.example.todo.model.service.AccountService
import kotlinx.coroutines.flow.Flow

class FakeAccountService(
) : AccountService {
    override val currentUserId: String
        get() {
            return "1"
        }
    override val hasUser: Boolean
        get() {
            TODO()
        }
    override val currentUser: Flow<User>
        get() {
            TODO()
        }

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
        TODO("Not yet implemented")
    }

    override suspend fun updateName(name: String) {
        TODO("Not yet implemented")
    }

    override suspend fun sendRecoveryEmail(email: String) {
        TODO("Not yet implemented")
    }
}