package com.example.todo.model.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable?)
}