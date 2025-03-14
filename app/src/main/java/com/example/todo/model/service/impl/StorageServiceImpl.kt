package com.example.todo.model.service.impl

import com.example.todo.model.Task
import com.example.todo.model.service.AccountService
import com.example.todo.model.service.StorageService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class StorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService
): StorageService {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val tasks: Flow<List<Task>>
        get() =
            auth.currentUser.flatMapLatest { user ->
                firestore
                    .collection(TASK_COLLECTION)
                    .whereEqualTo(USER_ID_FIELD, user.id)
                    .orderBy(CREATED_AT_FIELD, Query.Direction.DESCENDING)
                    .dataObjects()
            }

    override suspend fun getTask(taskId: String): Task? {
        TODO("Not yet implemented")
    }

    override suspend fun save(task: Task): String {
        val updatedTask = task.copy(userId = auth.currentUserId)
        return firestore.collection(TASK_COLLECTION).add(updatedTask).await().id
    }

    override suspend fun update(task: Task) {
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

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val COMPLETED_FIELD = "completed"
        private const val PRIORITY_FIELD = "priority"
        private const val FLAG_FIELD = "flag"
        private const val CREATED_AT_FIELD = "createdAt"
        private const val TASK_COLLECTION = "tasks"
        private const val SAVE_TASK_TRACE = "saveTask"
        private const val UPDATE_TASK_TRACE = "updateTask"
    }

}