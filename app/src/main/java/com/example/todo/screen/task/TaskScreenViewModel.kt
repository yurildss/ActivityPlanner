package com.example.todo.screen.task

import androidx.compose.runtime.mutableStateOf
import com.example.todo.model.Goals

class TaskScreenViewModel {
    var _taskScreenState = mutableStateOf(TaskScreenState())
        private set
}

data class TaskScreenState(
    val openDatePicker: Boolean = false,
    val title: String = "",
    val description: String = "",
    val deadLine: String = "",
    val priority: String = "",
    val gols: MutableList<Goals> = mutableListOf(),
    val tags: MutableList<String> = mutableListOf(),
    val timeToComplete: Long  = 0
)

data class GoalsScreenState(
    val openGoalsDatePicker: Boolean = false,
    val title: String = "",
    val description: String = "",
    val deadLine: String = "",
    val timeToComplete: String = "",
)