package com.example.todo.screen.login

import androidx.compose.runtime.mutableStateOf
import com.example.todo.common.SnackbarManager
import com.example.todo.common.isValidEmail
import com.example.todo.model.service.AccountService
import com.example.todo.model.service.LogService
import com.example.todo.screen.ToDoAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.todo.R.string as AppText

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
    ): ToDoAppViewModel(logService) {

    var uiState = mutableStateOf(LoginUiState())
        private set

    private val email get() = uiState.value.email
    private val password get() = uiState.value.password

    fun onEmailChange(newValue: String){
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String){
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onSignInClick(navigateToHome: () -> Unit){

        if(!email.isValidEmail()){
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if(password.isBlank()){
            SnackbarManager.showMessage(AppText.empty_password_error)
            return
        }

        launchCatching {
            accountService.authenticate(email, password)
            navigateToHome()
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
)