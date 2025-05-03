package com.example.todo.model.service.impl

import com.example.todo.model.User
import com.example.todo.model.service.AccountService
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore) : AccountService  {

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth ->
                val firebaseUser = auth.currentUser

                val user = if (firebaseUser != null) {
                    User(
                        id = firebaseUser.uid,
                        name = firebaseUser.displayName ?: "",
                        email = firebaseUser.email ?: ""
                    )
                } else {
                    User() // Usuário vazio ou não autenticado
                }

                trySend(user).isSuccess
            }

            Firebase.auth.addAuthStateListener(listener)
            awaitClose { Firebase.auth.removeAuthStateListener(listener) }
        }



    override suspend fun register(email: String, password: String, name: String) {
        Firebase
            .auth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    val user = auth.currentUser

                    val profilesUpdates = UserProfileChangeRequest
                        .Builder()
                        .setDisplayName(name)
                        .build()


                    user?.updateProfile(profilesUpdates)

                }else{
                    it.exception?.printStackTrace()
                }

        }

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

    override suspend fun updatePassword(password: String) {
        val user = auth.currentUser
        user!!.updatePassword(password).await()
    }

    override suspend fun updateName(name: String) {
        val user = auth.currentUser
        user!!.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build()).await()

    }

}