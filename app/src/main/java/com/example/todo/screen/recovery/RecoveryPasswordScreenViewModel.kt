package com.example.todo.screen.recovery

import androidx.compose.runtime.mutableStateOf
import com.example.todo.common.SnackbarManager
import com.example.todo.model.service.AccountService
import com.example.todo.model.service.LogService
import com.example.todo.screen.ToDoAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.todo.R.string as AppText
import javax.inject.Inject

@HiltViewModel
class RecoveryPasswordScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    private val logService: LogService
):ToDoAppViewModel()  {
    private val _uiState = MutableStateFlow(RecoveryPasswordUiState())
    val uiState = _uiState.asStateFlow()
    
    fun onEmailChange(newValue: String){
        _uiState.value = _uiState.value.copy(email = newValue)
    }
    
    fun sendRecoveryEmail(onEmailSend: () -> Unit){
        launchCatching {
            if(_uiState.value.email.isBlank()){
                SnackbarManager.showMessage(AppText.empty_email_error)
                return@launchCatching
            }
            accountService.sendRecoveryEmail(_uiState.value.email)
            onEmailSend()
        }
    }
}

data class RecoveryPasswordUiState(
    val email: String = ""
)