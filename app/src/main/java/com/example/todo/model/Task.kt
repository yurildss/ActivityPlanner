package com.example.todo.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import java.util.Date

data class Task(
    @DocumentId val id: String = "",
    @ServerTimestamp val createdAt: Date = Date(),
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val deadLine: Long = 0L,
    val priority: Int = 0,
    val tags: MutableList<String> = mutableListOf(),
    val gols: MutableList<Goals> = mutableListOf(),
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
){
    val dateInBrazilianFormat: String?
        get() = deadLine?.let { // Verifica se 'dateBirth' não é nulo
            Instant.fromEpochMilliseconds(it) // Converte 'dateBirth' (Long) em um objeto Instant
                .toLocalDateTime(TimeZone.UTC) // Transforma 'Instant' em uma data e hora local (UTC neste caso)
                .date // Extrai apenas a parte da data (sem hora)
                .format(LocalDate.Format { byUnicodePattern("dd/MM/yyyy") }) // Formata a data no padrão brasileiro
        }
}

data class Tags(
    val icon: String = "",
)