package com.example.todo.model.service.impl

import android.util.Log
import com.example.todo.model.Goals
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
        val snapshot = firestore
            .collection(TASK_COLLECTION)
            .document(taskId)
            .get()
            .await()

        return snapshot.toObject(Task::class.java)
    }


    override suspend fun save(task: Task): String {
        val updatedTask = task.copy(userId = auth.currentUserId)
        return firestore.collection(TASK_COLLECTION).add(updatedTask).await().id
    }

    override suspend fun update(task: Task, taskId: String) {

        val taskRef = firestore.collection(TASK_COLLECTION).document(taskId)
        taskRef.set(task).await()

    }

    override suspend fun updateTaskCompleted(taskId: String, completed: Boolean) {
        val taskRef = firestore.collection(TASK_COLLECTION).document(taskId)
        taskRef.update("completed", completed).await()
    }

    override suspend fun updateTaskNotification(taskId: String, notificationRead: Boolean) {
        val taskRef = firestore.collection(TASK_COLLECTION).document(taskId)
        taskRef.update("notificationRead", notificationRead).await()
    }

    override suspend fun getCompletedTask(): List<Task> {
        val taskRef = firestore.collection(TASK_COLLECTION)
        val snapshot = taskRef
            .whereEqualTo("completed", true)
            .get()
            .await()
        return snapshot.toObjects(Task::class.java)
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

    override suspend fun updateGoalPercent(taskId: String, goalId: Int, percent: Float) {
        val taskRef = firestore.collection(TASK_COLLECTION).document(taskId)
        val snapshot = taskRef.get().await()

        val task = snapshot.toObject(Task::class.java)

        if (task != null && goalId in task.goals.indices) {
            val updatedGols = task.goals.toMutableList()

            if(percent == 1F){
                updatedGols[goalId] = updatedGols[goalId].copy(completed = true, percentComplete = percent)
            }
            else{
                updatedGols[goalId] = updatedGols[goalId].copy(percentComplete = percent, completed = false)
            }


            taskRef.update("goals", updatedGols).await()
        }
    }

    override suspend fun getDelayedTasks(): List<Task> {
        val taskRef = firestore.collection(TASK_COLLECTION)
        val now = System.currentTimeMillis()

        val snapshot = taskRef
            .whereLessThan(DEADLINE_FIELD, now)
            .whereEqualTo("completed", false)
            .whereEqualTo("notificationRead", false)
            .get()
            .await()
        return snapshot.toObjects(Task::class.java)
    }

    override suspend fun getDelayedGoals(taskId: String): List<Goals> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskByDay(day: String): List<Task> {
        return firestore
            .collection(TASK_COLLECTION)
            .whereEqualTo(DEADLINE_FIELD_STRING, day)
            .get()
            .await()
            .toObjects(Task::class.java)
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
        private const val DEADLINE_FIELD = "deadLine"
        private const val DEADLINE_FIELD_STRING = "dateInBrazilianFormat"
        private const val TASK_ID_FIELD = "id"
    }

}