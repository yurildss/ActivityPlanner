package com.example.todo
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import org.junit.jupiter.api.Assertions.assertTrue

class FirebaseAuthTest {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        firebaseAuth = mockk()
        firestore = mockk()
        userRepository = UserRepository(firebaseAuth, firestore)
    }

    @Test
    fun `test register success`() = runBlocking {
        // Mockando o comportamento do FirebaseAuth
        val mockUser = mockk<FirebaseUser>()
        val mockAuthResult = mockk<AuthResult>()
        every { firebaseAuth.createUserWithEmailAndPassword(any(), any()) } returns mockk {
            coEvery { await() } returns mockAuthResult
        }
        every { mockAuthResult.user } returns mockUser
        every { mockUser.uid } returns "mockUserId"

        // Mockando o comportamento do Firestore
        val mockDocumentReference = mockk<com.google.firebase.firestore.DocumentReference>()
        every { firestore.collection("users").document("mockUserId").set(any()) } returns mockk {
            // Mockando o comportamento do Firestore
            justRun { firestore.collection("users").document("mockUserId").set(any()) }
        }

        // Chamada do método de registro
        val email = "test@test.com"
        val password = "password123"
        val name = "Test User"

        userRepository.register(email, password, name)

        // Verificando se a criação de usuário foi chamada
        verify { firebaseAuth.createUserWithEmailAndPassword(email, password) }

        // Verificando se o Firestore foi chamado para salvar o usuário
        verify { firestore.collection("users").document("mockUserId").set(any()) }
    }

    @Test
    fun `test register failure`() = runBlocking {
        // Mockando o erro ao criar o usuário
        every { firebaseAuth.createUserWithEmailAndPassword(any(), any()) } throws Exception("Registration failed")

        val email = "test@test.com"
        val password = "password123"
        val name = "Test User"

        try {
            userRepository.register(email, password, name)
        } catch (e: Exception) {
            // Verifica que o erro foi corretamente lançado
            assertTrue { e.message == "Registration failed" }
        }
    }
}

class UserRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun register(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
        val userId = auth.currentUser!!.uid
        val user = User(id = userId, name = name)
        firestore.collection("users").document(userId).set(user).await()
    }
}

data class User(
    val id: String,
    val name: String
)
