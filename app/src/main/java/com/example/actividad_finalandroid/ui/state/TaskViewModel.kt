package com.example.actividad_finalandroid.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.actividad_finalandroid.data.local.entity.TaskDraftEntity
import com.example.actividad_finalandroid.domain.model.Task
import com.example.actividad_finalandroid.domain.usecase.auth.GetCurrentUserUseCase
import com.example.actividad_finalandroid.domain.usecase.draft.GetDraftsUseCase
import com.example.actividad_finalandroid.domain.usecase.draft.PublishDraftUseCase
import com.example.actividad_finalandroid.domain.usecase.draft.SaveDraftUseCase
import com.example.actividad_finalandroid.domain.usecase.task.CreateTaskUseCase
import com.example.actividad_finalandroid.domain.usecase.task.DeleteTaskUseCase
import com.example.actividad_finalandroid.domain.usecase.task.GetTasksUseCase
import com.example.actividad_finalandroid.domain.usecase.task.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(
    private val createTaskUseCase: CreateTaskUseCase,
    private val getTasksUseCase: GetTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val saveDraftUseCase: SaveDraftUseCase,
    private val getDraftsUseCase: GetDraftsUseCase,
    private val publishDraftUseCase: PublishDraftUseCase,
    getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    val ownerId: String = getCurrentUserUseCase() ?: ""

    val pendingDraftsState: StateFlow<List<TaskDraftEntity>> = getDraftsUseCase(ownerId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _syncMessage = MutableStateFlow<String?>(null)
    val syncMessage: StateFlow<String?> = _syncMessage.asStateFlow()

    fun addTask(title: String, description: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            val draft = TaskDraftEntity(
                ownerId = ownerId,
                title = title,
                description = description,
                savedAt = System.currentTimeMillis()
            )
            saveDraftUseCase(draft)
            _syncMessage.value = "Tarea guardada localmente. Presiona 'Subir a Firebase' para sincronizar."
        }
    }

    fun uploadAllToFirebase() {
        val pending = pendingDraftsState.value
        if (pending.isEmpty()) {
            _syncMessage.value = "No hay tareas pendientes por subir."
            return
        }
        viewModelScope.launch {
            _isSyncing.value = true
            _syncMessage.value = "Subiendo tareas a Firebase..."
            var count = 0
            pending.forEach { draft ->
                val result = publishDraftUseCase(draft)
                if (result.isSuccess) count++
            }
            _isSyncing.value = false
            _syncMessage.value = "¡Se subieron $count tareas a Firebase exitosamente!"
        }
    }

    fun uploadSingleDraftToFirebase(draft: TaskDraftEntity) {
        viewModelScope.launch {
            _isSyncing.value = true
            publishDraftUseCase(draft)
                .onSuccess {
                    _syncMessage.value = "Tarea '${draft.title}' subida a Firebase."
                }
                .onFailure {
                    _syncMessage.value = "Error al subir la tarea a Firebase."
                }
            _isSyncing.value = false
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(completed = !task.completed)
            updateTaskUseCase(updatedTask)
        }
    }

    fun updateTaskDetails(task: Task, title: String, description: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            val updatedTask = task.copy(
                title = title,
                description = description
            )
            updateTaskUseCase(updatedTask)
        }
    }

    fun removeTask(id: String) {
        if (id.isBlank()) return
        viewModelScope.launch {
            deleteTaskUseCase(id)
        }
    }

    fun clearSyncMessage() {
        _syncMessage.value = null
    }
}
