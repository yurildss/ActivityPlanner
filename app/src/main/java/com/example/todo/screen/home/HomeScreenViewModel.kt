package com.example.todo.screen.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.todo.model.Task
import com.example.todo.model.User
import com.example.todo.model.service.AccountService
import com.example.todo.model.service.StorageService
import com.example.todo.screen.ToDoAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val storageService: StorageService,
    accountService: AccountService,
) : ToDoAppViewModel() {

    var uiState = mutableStateOf(HomeScreenUiState())
        private set


    init {
        launchCatching {

            val user = accountService.currentUser.first()
            userName(user.name)

            uiState.value = uiState.value.copy(
                tasksOfTheDay = storageService.getTaskByDay(uiState.value.actualDay)
            )
            delayTask()
        }
    }

        //Modificar para receber as Tasks do dia que está no calendario, e esse dia
        // pode ser modificado com a interação do usuario com o calendario

        private val searchText
            get()  = uiState.value.searchText

        fun onSearchTextChange(text: String){
            uiState.value = uiState.value.copy(searchText = text)
        }

         private fun userName(userName: String){
            uiState.value = uiState.value.copy(
                name = userName
            )
        }

        private suspend fun delayTask(){
               val delayedTasks = storageService.getDelayedTasks().size
                uiState.value = uiState.value.copy(
                    notifications = delayedTasks
                )
        }

        fun setOpenDatePicker(newValue: Boolean){
            uiState.value =
                uiState.value.copy(openDatePicker = newValue)
        }

    fun updateTaskDeadLine(newValue: String) {
        launchCatching {
            uiState.value = uiState.value.copy(actualDay = newValue)
            val tasks = storageService.getTaskByDay(newValue)
            uiState.value = uiState.value.copy(tasksOfTheDay = tasks)
        }
    }
}

data class HomeScreenUiState(
    val name: String = "",
    val searchText: String = "",
    val pendingTask: Int = 0,
    val openDatePicker: Boolean = false,
    val actualDay: String = Clock
        .System
        .now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
        .format(LocalDate.Format { byUnicodePattern("dd/MM/yyyy") }),
    val tasksOfTheDay: List<Task> = emptyList(),
    val notifications: Int = 0
)