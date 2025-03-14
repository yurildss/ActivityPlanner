package com.example.todo

import com.example.todo.model.service.impl.AccountServiceImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AccountServiceImplIntegrationTest {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var accountService: AccountServiceImpl

    @Before
    fun setUp() {
        // Configura o Firebase para usar o Emulator
        Firebase.auth.useEmulator("10.0.2.2", 9099) // 10.0.2.2 é o IP padrão para o localhost no Android Emulator
        Firebase.firestore.useEmulator("10.0.2.2", 8080)

        // Inicializa as instâncias do Firebase
        auth = Firebase.auth
        firestore = Firebase.firestore

        // Limpa o estado do Emulator antes de cada teste
        auth.signOut()
        firestore.clearPersistence()

        // Inicializa o serviço
        accountService = AccountServiceImpl(auth, firestore)
    }

    @After
    fun tearDown() {
        // Limpa o estado do Emulator após cada teste
        auth.signOut()
        firestore.clearPersistence()
    }

    @Test
    fun `register should create a new user and update profile`() = runBlocking {
        // Dados de teste
        val email = "test@example.com"
        val password = "password123"
        val name = "Test User"

        // Executa o método de registro
        accountService.register(email, password, name)

        // Verifica se o usuário foi criado
        val user = auth.currentUser
        assertTrue(user != null, "O usuário não foi criado")
        assertEquals(email, user?.email, "O e-mail do usuário não corresponde")
        assertEquals(name, user?.displayName, "O nome do usuário não foi atualizado")
    }

    @Test
    fun `authenticate should sign in an existing user`() = runBlocking {
        // Dados de teste
        val email = "test@example.com"
        val password = "password123"

        // Cria um usuário para teste
        auth.createUserWithEmailAndPassword(email, password).await()

        // Executa o método de autenticação
        accountService.authenticate(email, password)

        // Verifica se o usuário está autenticado
        val user = auth.currentUser
        assertTrue(user != null, "O usuário não foi autenticado")
        assertEquals(email, user?.email, "O e-mail do usuário não corresponde")
    }

    @Test
    fun `createAnonymousAccount should sign in anonymously`() = runBlocking {
        // Executa o método de criação de conta anônima
        accountService.createAnonymousAccount()

        // Verifica se o usuário está autenticado
        val user = auth.currentUser
        assertTrue(user != null, "O usuário anônimo não foi criado")
        assertTrue(user?.isAnonymous == true, "O usuário não é anônimo")
    }

    @Test
    fun `linkAccount should convert anonymous account to permanent account`() = runBlocking {
        // Cria uma conta anônima
        accountService.createAnonymousAccount()

        // Dados de teste
        val email = "test@example.com"
        val password = "password123"

        // Executa o método de vinculação de conta
        accountService.linkAccount(email, password)

        // Verifica se a conta foi vinculada
        val user = auth.currentUser
        assertTrue(user != null, "O usuário não está autenticado")
        assertFalse(user?.isAnonymous == true, "A conta ainda é anônima")
        assertEquals(email, user?.email, "O e-mail do usuário não corresponde")
    }
}