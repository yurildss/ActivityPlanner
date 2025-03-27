package com.example.todo.model.service

import com.example.todo.model.Task
import kotlinx.coroutines.flow.Flow


interface StorageService {
    val tasks: Flow<List<Task>>
    suspend fun getTask(taskId: String): Task?
    suspend fun save(task: Task): String
    suspend fun update(task: Task)
    suspend fun delete(taskId: String)
    suspend fun getCompletedTasksCount(): Int
    suspend fun getImportantCompletedTasksCount(): Int
    suspend fun getMediumHighTasksToCompleteCount(): Int
    suspend fun getIncompleteTasksCount(): Int
    suspend fun getTaskByDay(day: String): Flow<List<Task>>
}