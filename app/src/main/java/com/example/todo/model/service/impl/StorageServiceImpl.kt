package com.example.todo.model.service.impl

import android.util.Log
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
        Log.d("TAG", "save: ${auth.currentUserId}")
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

    override suspend fun updateGoalPercent(taskId: String, goalIndex: Int, percent: Float) {
        val taskRef = firestore.collection(TASK_COLLECTION).document(taskId)
        val snapshot = taskRef.get().await()
        Log.d("TESTE", "Raw Data: ${snapshot.data}")
        val task = snapshot.toObject(Task::class.java)
        Log.d("TESTE", "isCompleted no Firestore: ${snapshot.data?.get("goals")}")
        Log.d("TESTE", "isCompleted convertido (Task): ${task?.goals}")

        if (task != null && goalIndex in task.goals.indices) {
            val updatedGols = task.goals.toMutableList()

            if(percent == 1F){
                updatedGols[goalIndex] = updatedGols[goalIndex].copy(completed = true, percentComplete = percent)
            }
            else{
                Log.d("TAG", "Else")
                updatedGols[goalIndex] = updatedGols[goalIndex].copy(percentComplete = percent, completed = false)
            }


            taskRef.update("goals", updatedGols).await()
            Log.d("TAG", "updateGoalPercent: $updatedGols")
        }
    }

    override suspend fun getTaskByDay(day: String): List<Task> {
        Log.d("TAG", "getTaskByDay: $day")
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