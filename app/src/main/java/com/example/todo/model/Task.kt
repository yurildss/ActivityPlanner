package com.example.todo.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Task(
    @DocumentId val id: String = "",
    @ServerTimestamp val createdAt: Date = Date(),
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val deadLine: Long = 0L,
    val priority: Int = 0,
    val tags: List<Tags> = emptyList(),
    val gols: List<Goals> = emptyList(),
    val userId: String = "",
    val timeToComplete: Long = 0L,
)

data class Goals(
    val createdAt: Date = Date(),
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val deadLine: Long = 0L ,
    val timeToComplete: Long = 0L,
    val isSave: Boolean = false,
)

data class Tags(
    val icon: String = "",
)