package com.example.todo

import com.example.todo.model.service.AccountService
import com.example.todo.screen.login.LoginScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@Suppress("IllegalIdentifier")
class LoginScreenViewModelUnitTest {
    private val accountService = mock<AccountService>()
    private lateinit var viewModel: LoginScreenViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)

        runTest{
            whenever(accountService.authenticate(any(), any())).thenReturn(Unit)
        }

        viewModel = LoginScreenViewModel(accountService)
    }

    @Test
    fun `should update email field`(){
        viewModel.onEmailChange("test")
        assert(viewModel.uiState.value.email == "test")
    }

    @Test
    fun `should update password field`(){
        viewModel.onPasswordChange("test")
        assert(viewModel.uiState.value.password == "test")
    }

    @Test
    fun `should authenticate user`(){
        var navigateToHomeCalled = false
        viewModel.onEmailChange("test@hotmail.com")
        viewModel.onPasswordChange("test")
        viewModel.onSignInClick{
            navigateToHomeCalled = true
        }
        assert(navigateToHomeCalled)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }
}
