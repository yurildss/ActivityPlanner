package com.example.todo.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import java.util.Date

data class Goals(
    val createdAt: Date = Date(),
    val title: String = "",
    val description: String = "",
    val completed: Boolean = false,
    val deadLine: Long = 0L,
    val timeToComplete: Long = 0L,
    val percentComplete: Float = 0F,
    val isSave: Boolean = false,
){
    val dateInBrazilianFormat: String
        get() = deadLine.let { // Verifica se 'dateBirth' não é nulo
            Instant.fromEpochMilliseconds(it) // Converte 'dateBirth' (Long) em um objeto Instant
                .toLocalDateTime(TimeZone.UTC) // Transforma 'Instant' em uma data e hora local (UTC neste caso)
                .date // Extrai apenas a parte da data (sem hora)
                .format(LocalDate.Format { byUnicodePattern("dd/MM/yyyy") }) // Formata a data no padrão brasileiro
        }
}