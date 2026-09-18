package com.example.actividad_finalandroid.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.actividad_finalandroid.domain.model.Task
import com.example.actividad_finalandroid.domain.usecase.auth.GetCurrentUserUseCase
import com.example.actividad_finalandroid.domain.usecase.task.CreateTaskUseCase
import com.example.actividad_finalandroid.domain.usecase.task.DeleteTaskUseCase
import com.example.actividad_finalandroid.domain.usecase.task.GetTasksUseCase
import com.example.actividad_finalandroid.domain.usecase.task.UpdateTaskUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(
    private val createTaskUseCase: CreateTaskUseCase,
    private val getTasksUseCase: GetTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    val ownerId: String = getCurrentUserUseCase() ?: ""

    val taskUiState: StateFlow<TaskUiState> = getTasksUseCase(ownerId)
        .map { tasks ->
            if (tasks.isEmpty()) TaskUiState.Empty else TaskUiState.Success(tasks)
        }
        .catch { exception ->
            emit(TaskUiState.Error(exception.localizedMessage ?: "Error al cargar tareas."))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TaskUiState.Loading
        )

    fun addTask(title: String, description: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            val newTask = Task(
                ownerId = ownerId,
                title = title,
                description = description
            )
            createTaskUseCase(newTask)
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(completed = !task.completed)
            updateTaskUseCase(updatedTask)
        }
    }

    fun removeTask(id: String) {
        if (id.isBlank()) return
        viewModelScope.launch {
            deleteTaskUseCase(id)
        }
    }
}
