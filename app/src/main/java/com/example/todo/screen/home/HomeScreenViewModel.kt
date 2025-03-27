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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
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

    //Preciso fazer com que o init carrega também todas as tarefas do dia que está no calendario

    init {
        launchCatching {
            accountService.currentUser
                .onEach { user -> user.let { userName(it.name) } } // ✅
                .stateIn(viewModelScope, SharingStarted.Eagerly, User())

            uiState.value = uiState.value.copy(
                pendingTask = storageService.getIncompleteTasksCount()
            )

        }
    }


    var uiState = mutableStateOf(HomeScreenUiState())
            private set

        //Modificar para receber as Tasks do dia que está no calendario, e esse dia
        // pode ser modificado com a interação do usuario com o calendario
        val tasks = storageService.tasks

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

        fun setOpenDatePicker(newValue: Boolean){
            uiState.value =
                uiState.value.copy(openDatePicker = newValue)


        }

        fun updateTaskDeadLine(newValue: String){
            /**
             * Converte a data no formato dd/MM/yyyy para LocalDate do kotlinx.datetime
             * */
            val parsedDate = uiState.value.actualDay.split("/").let { parts ->
                LocalDate(parts[2].toInt(), parts[1].toInt(), parts[0].toInt())
            }

            uiState.value =
                uiState.value.copy(actualDay = newValue)

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
    val tasksOfTheDay: MutableList<Task> = mutableListOf()
)