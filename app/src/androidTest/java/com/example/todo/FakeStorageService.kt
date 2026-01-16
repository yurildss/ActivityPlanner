package com.example.todo

import com.example.todo.model.Goals
import com.example.todo.model.Task
import com.example.todo.model.service.StorageService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow


class FakeStorageService : StorageService {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val tasks: Flow<List<Task>>
        get() =
            TODO("Not yet implemented")


    override suspend fun getTask(taskId: String): Task? {
        TODO("Not yet implemented")
    }

    override suspend fun save(task: Task): String {
        TODO("Not yet implemented")
    }

    override suspend fun update(task: Task, taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTaskCompleted(taskId: String, completed: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getCompletedTasksCount(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getImportantCompletedTasksCount(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getMediumHighTasksToCompleteCount(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getIncompleteTasksCount(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskByDay(day: String): List<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun updateGoalPercent(
        taskId: String,
        goalId: Int,
        percent: Float
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getDelayedTasks(): List<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun getDelayedGoals(taskId: String): List<Goals> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTaskNotification(
        taskId: String,
        notificationRead: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getCompletedTask(): List<Task> {
        TODO("Not yet implemented")
    }
}