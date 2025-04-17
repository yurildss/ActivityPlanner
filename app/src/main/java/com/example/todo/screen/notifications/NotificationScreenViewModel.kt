package com.example.todo.screen.notifications

import androidx.compose.runtime.mutableStateOf
import com.example.todo.model.Task
import com.example.todo.screen.ToDoAppViewModel

class NotificationScreenViewModel : ToDoAppViewModel(){
    var notificationScreenState = mutableStateOf(NotificationScreenState())
        private set
}

data class NotificationScreenState(
    val taskList: List<Task> = emptyList()
)