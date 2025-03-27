package com.example.todo.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.common.SnackbarManager
import com.example.todo.common.SnackbarMessage.Companion.toSnackbarMessage
import com.example.todo.model.service.LogService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


open class ToDoAppViewModel() : ViewModel() {
    fun launchCatching(snackbar: Boolean = true, block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                if (snackbar) {
                    SnackbarManager.showMessage(throwable.toSnackbarMessage())
                    Log.d("TAG", "launchCatching: $throwable")
                }
            },
            block = block
        )
}