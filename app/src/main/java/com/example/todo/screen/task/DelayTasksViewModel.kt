package com.example.todo.screen.task

import androidx.compose.runtime.mutableStateOf
import com.example.todo.model.Task
import com.example.todo.model.service.StorageService
import com.example.todo.screen.ToDoAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DelayTasksViewModel
@Inject constructor(storageService: StorageService) : ToDoAppViewModel()
{
    var delayTaskUiState = mutableStateOf(DelayTaskUiState())
        private set

    init {
        launchCatching {
            delayTaskUiState.value = DelayTaskUiState(
                tasks = storageService.getDelayedTasks()
            )
        }
    }

}

data class DelayTaskUiState(
    val tasks: List<Task> = emptyList()
)