package com.example.todo.screen.sign_up

import androidx.compose.runtime.mutableStateOf
import com.example.todo.R
import com.example.todo.common.SnackbarManager
import com.example.todo.common.isValidEmail
import com.example.todo.model.service.AccountService
import com.example.todo.model.service.LogService
import com.example.todo.screen.ToDoAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    private val accountService: AccountService,
): ToDoAppViewModel() {

    var uiState = mutableStateOf(SignUpUiState())
        private set

    private val email get() = uiState.value.email
    private val password get() = uiState.value.password
    private val name get() = uiState.value.name

    fun onEmailChange(newValue: String){
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String){
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String){
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onNameChange(newValue: String){
        uiState.value = uiState.value.copy(name = newValue)
    }

    fun onSignUpClick(navigateToSingIn: () -> Unit){
        if(!email.isValidEmail()){
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        if(password.isBlank()){
            SnackbarManager.showMessage(R.string.empty_password_error)
            return
        }

        if(password != uiState.value.repeatPassword){
            SnackbarManager.showMessage(R.string.password_match_error)
            return
        }

        launchCatching {
            accountService.register(email, password, name)
            navigateToSingIn()
        }
    }
}

data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
)