package com.example.todo.screen.notifications

import androidx.compose.runtime.mutableStateOf
import com.example.todo.model.Task
import com.example.todo.model.service.StorageService
import com.example.todo.screen.ToDoAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
                taskList = withContext(Dispatchers.IO) {taskRepository.getDelayedTasks()}
            )
        }
    }

    fun updateTaskNotification(taskId: String, notificationRead: Boolean){
        launchCatching {
            withContext(Dispatchers.IO){taskRepository.updateTaskNotification(taskId, notificationRead)}
            notificationScreenState.value = NotificationScreenState(
                taskList = withContext(Dispatchers.IO) {taskRepository.getDelayedTasks()}
            )
        }
    }
}



data class NotificationScreenState(
    val taskList: List<Task> = emptyList()
)