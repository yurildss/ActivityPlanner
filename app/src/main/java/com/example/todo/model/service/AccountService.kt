package com.example.todo.model.service

interface AccountService {
    suspend fun register(email: String, password: String)
    suspend fun createAnonymousAccount()
    suspend fun authenticate(email: String, password: String)
    suspend fun linkAccount(email: String, password: String)
}