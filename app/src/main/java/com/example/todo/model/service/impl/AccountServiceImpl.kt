package com.example.todo.model.service.impl

import com.example.todo.model.User
import com.example.todo.model.service.AccountService
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
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
        get() = TODO("Not yet implemented")
    override val currentUser: Flow<User> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                // Buscar dados do usuário no Firestore
                firestore.collection("users").document(firebaseUser.uid)
                    //O metodo addSnapshotListener do Firestore é usado para escutar atualizações
                    // em tempo real de um documento ou coleção no banco de dados Firestore.
                    // Sempre que os dados mudam, o listener é chamado automaticamente.
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            this.trySend(User()) // Retorna usuário vazio se houver erro
                            return@addSnapshotListener
                        }
                        val user = snapshot?.toObject(User::class.java) ?: User()
                        this.trySend(user)
                    }
            } else {
                this.trySend(User()) // Se não estiver autenticado, retorna usuário vazio
            }
        }

        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }


    override suspend fun register(email: String, password: String, name: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).await()
        val userId = auth.currentUser!!.uid

        // Criar um objeto de usuário para salvar no Firestore
        val user = User(
            id = userId,
            name = name,
        )
        // Salvar no Firestore
        firestore.collection("users").document(userId).set(user).await()
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