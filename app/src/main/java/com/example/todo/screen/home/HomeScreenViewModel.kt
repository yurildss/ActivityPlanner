package com.example.todo.screen.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.todo.model.User
import com.example.todo.model.service.AccountService
import com.example.todo.model.service.LogService
import com.example.todo.model.service.StorageService
import com.example.todo.screen.ToDoAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val storageService: StorageService,
    accountService: AccountService,
) : ToDoAppViewModel() {

    init {
        launchCatching {
            uiState.value = uiState.value.copy(
                pendingTask = storageService.getIncompleteTasksCount()
            )
        }
    }

    private val currentUser: StateFlow<User?> = accountService.currentUser
        .onEach { user -> user.let { userName(it.name) } } // ✅
        .stateIn(viewModelScope, SharingStarted.Eagerly, User())


    var uiState = mutableStateOf(HomeScreenUiState())
            private set

        val tasks = storageService.tasks
        private val searchText get()  = uiState.value.searchText

        fun onSearchTextChange(text: String){
            uiState.value = uiState.value.copy(searchText = text)
        }

         private fun userName(userName: String){
            uiState.value = uiState.value.copy(
                name = userName
            )
        }

}

data class HomeScreenUiState(
    val name: String = "",
    val searchText: String = "",
    val pendingTask: Int = 0,
)