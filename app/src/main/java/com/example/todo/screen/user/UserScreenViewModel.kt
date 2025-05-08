package com.example.todo.screen.user

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.todo.R.string
import com.example.todo.common.SnackbarManager
import com.example.todo.common.isValidEmail
import com.example.todo.model.service.AccountService
import com.example.todo.model.service.StorageService
import com.example.todo.screen.ToDoAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import com.example.todo.R.string as AppText


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
    private val oldPassword get() = uiState.value.oldPassword

    fun onNameChange(newValue: String){
        uiState.value = uiState.value.copy(name = newValue)
    }

    fun onOldPasswordChange(newValue: String){
        uiState.value = uiState.value.copy(oldPassword = newValue)
    }

    fun onRepeatPasswordChange(newValue: String){
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onPasswordChange(newValue: String){
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onNewPasswordChange(newValue: String){

        if(oldPassword.isBlank()){
            SnackbarManager.showMessage(AppText.empty_password_error)
            return
        }

        if(password.isBlank()){
            SnackbarManager.showMessage(AppText.empty_password_error)
            return
        }
        if(password != uiState.value.repeatPassword){
            SnackbarManager.showMessage(AppText.password_match_error)
            return
        }
        launchCatching {
            Log.d("UserScreenViewModel", "onNewPasswordChange: $email")
            accountService.updatePassword(email, newValue, oldPassword)
            SnackbarManager.showMessage(AppText.password_change_success)
            uiState.value = uiState.value.copy(password = "", repeatPassword = "", oldPassword = "")
        }
    }

    fun onSaveButtonClick(){
        if(name.isBlank()){
            SnackbarManager.showMessage(AppText.empty_field_task)
            return
        }

        launchCatching {
            accountService.updateName(name)
            SnackbarManager.showMessage(AppText.name_change_success)
        }
    }
}

data class  UserScreenUiState(
    val name: String = "",
    val email: String = "",
    val oldPassword: String = "",
    val password: String = "",
    val repeatPassword: String = ""
)