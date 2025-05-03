package com.example.todo.screen.user

import androidx.compose.runtime.mutableStateOf
import com.example.todo.common.isValidEmail
import com.example.todo.model.service.AccountService
import com.example.todo.model.service.StorageService
import com.example.todo.screen.ToDoAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class UserScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    private val storageService: StorageService
): ToDoAppViewModel() {

    var uiState = mutableStateOf(UserScreenUiState())
        private set

    init {

        launchCatching{
            uiState.value = uiState.value.copy(
                name = accountService.currentUser.first().name,
                email = accountService.currentUser.first().email
            )
        }

    }

    private val email get() = uiState.value.email
    private val password get() = uiState.value.password
    private val name get() = uiState.value.name

    fun onNameChange(newValue: String){
        uiState.value = uiState.value.copy(name = newValue)
    }

    fun onEmailChange(newValue: String){
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onRepeatPasswordChange(newValue: String){
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onPasswordChange(newValue: String){
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onNewPasswordChange(newValue: String){
        if(password.isBlank()){
            return
        }
        if(password != uiState.value.repeatPassword){
            return
        }
        if(!email.isValidEmail()){
            return
        }
        launchCatching {
            accountService.updatePassword(newValue)
        }
    }

    fun onSaveButtonClick(){
        if(name.isBlank()){
            return
        }

        launchCatching {
            accountService.updateName(name)
        }
    }
}

data class  UserScreenUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = ""
)