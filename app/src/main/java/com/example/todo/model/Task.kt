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
    val deadLine: Date = Date(),
    val priority: Int = 0,
    val tags: List<Tags> = emptyList(),
    val gols: List<Gols> = emptyList(),
    val userId: String = "",
    val timeToComplete: Long = 0L,
)

data class Gols(
    val createdAt: Date = Date(),
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val timeToComplete: Long = 0L,
)

data class Tags(
    val title: String = "",
    val icon: String = "",
)