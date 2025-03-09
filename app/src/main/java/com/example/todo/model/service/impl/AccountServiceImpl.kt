package com.example.todo.model.service.impl

import com.example.todo.model.service.AccountService
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(private val auth: FirebaseAuth) : AccountService  {

    override suspend fun register(email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun createAnonymousAccount() {
        Firebase.auth.signInAnonymously().await()
    }

    /**
     * O método signInWithEmailAndPassword(email, password)
     * tenta fazer login com as credenciais informadas.
     */
    override suspend fun authenticate(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .await()
    }

    /**
     * Converte uma conta anônima em uma conta com e-mail e senha.
     */
    override suspend fun linkAccount(email: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)
        Firebase.auth.currentUser!!.linkWithCredential(credential)
            .await()
    }

}