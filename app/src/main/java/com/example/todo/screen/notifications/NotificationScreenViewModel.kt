package com.example.todo.screen.notifications

import androidx.compose.runtime.mutableStateOf
import com.example.todo.model.Task
import com.example.todo.model.service.StorageService
import com.example.todo.screen.ToDoAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationScreenViewModel @Inject constructor(
    private val taskRepository: StorageService
) : ToDoAppViewModel(){
    var notificationScreenState = mutableStateOf(NotificationScreenState())
        private set

    init {
        launchCatching {
            notificationScreenState.value = NotificationScreenState(
                taskList = taskRepository.getDelayedTasks()
            )
        }
    }

    fun updateTaskNotification(taskId: String, notificationRead: Boolean){
        launchCatching {
            taskRepository.uptadeTaskNotification(taskId, notificationRead)
            notificationScreenState.value = NotificationScreenState(
                taskList = taskRepository.getDelayedTasks()
            )
        }
    }
}



data class NotificationScreenState(
    val taskList: List<Task> = emptyList()
)