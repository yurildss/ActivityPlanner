package com.example.todo.model.service

import com.example.todo.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean
    val currentUser: Flow<User>

    suspend fun register(email: String, password: String, name: String)
    suspend fun createAnonymousAccount()
    suspend fun authenticate(email: String, password: String)
    suspend fun linkAccount(email: String, password: String)
    suspend fun updatePassword(email: String, password: String, oldPassword: String)
    suspend fun updateName(name: String)
    suspend fun sendRecoveryEmail(email: String)
}