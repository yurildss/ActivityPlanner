package com.example.todo.screen.task

import androidx.compose.runtime.mutableStateOf
import com.example.todo.model.Task
import com.example.todo.model.service.StorageService
import com.example.todo.screen.ToDoAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompletedTasksViewModel @Inject constructor(storageService: StorageService) : ToDoAppViewModel() {

    var completedTaskUiState = mutableStateOf(CompletedTaskUiState())
    private set

    init {
        launchCatching {
            completedTaskUiState.value = CompletedTaskUiState(
                tasks = storageService.getDelayedTasks()
            )
        }
    }

}


data class CompletedTaskUiState(
    val tasks: List<Task> = emptyList()
)