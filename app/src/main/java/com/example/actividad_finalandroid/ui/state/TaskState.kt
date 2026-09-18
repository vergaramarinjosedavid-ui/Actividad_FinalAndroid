package com.example.actividad_finalandroid.ui.state

import com.example.actividad_finalandroid.domain.model.Task

sealed interface TaskUiState {
    data object Loading : TaskUiState
    data object Empty : TaskUiState
    data class Success(val tasks: List<Task>) : TaskUiState
    data class Error(val message: String) : TaskUiState
}
