package com.example.todo.common

import android.util.Patterns
import java.util.regex.Pattern

private const val MIN_PASS_LENGTH = 6
private const val PASS_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$"

fun String.isValidEmail(): Boolean {
    return try {
        android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    } catch (e: Exception) {
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        this.matches(regex)
    }
}

fun String.isValidPassword(): Boolean {
    return this.isNotBlank()
            && this.length >= MIN_PASS_LENGTH
            && Pattern.compile(PASS_PATTERN).matcher(this).matches()
}

