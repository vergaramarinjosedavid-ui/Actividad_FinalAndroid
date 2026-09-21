package com.example.actividad_finalandroid.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.actividad_finalandroid.data.local.entity.TaskDraftEntity
import com.example.actividad_finalandroid.domain.usecase.auth.GetCurrentUserUseCase
import com.example.actividad_finalandroid.domain.usecase.draft.DeleteDraftUseCase
import com.example.actividad_finalandroid.domain.usecase.draft.GetDraftsUseCase
import com.example.actividad_finalandroid.domain.usecase.draft.PublishDraftUseCase
import com.example.actividad_finalandroid.domain.usecase.draft.SaveDraftUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface DraftActionStatus {
    data object Idle : DraftActionStatus
    data object Loading : DraftActionStatus
    data object Success : DraftActionStatus
    data class Error(val message: String) : DraftActionStatus
}

class DraftViewModel(
    private val getDraftsUseCase: GetDraftsUseCase,
    private val saveDraftUseCase: SaveDraftUseCase,
    private val deleteDraftUseCase: DeleteDraftUseCase,
    private val publishDraftUseCase: PublishDraftUseCase,
    getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val ownerId: String = getCurrentUserUseCase() ?: ""

    val draftsState: StateFlow<List<TaskDraftEntity>> = getDraftsUseCase(ownerId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _actionStatus = MutableStateFlow<DraftActionStatus>(DraftActionStatus.Idle)
    val actionStatus: StateFlow<DraftActionStatus> = _actionStatus.asStateFlow()

    fun saveNewDraft(title: String, description: String) {
        if (title.isBlank()) {
            _actionStatus.value = DraftActionStatus.Error("El título es obligatorio.")
            return
        }
        viewModelScope.launch {
            val newDraft = TaskDraftEntity(
                ownerId = ownerId,
                title = title,
                description = description,
                savedAt = System.currentTimeMillis()
            )
            saveDraftUseCase(newDraft)
            _actionStatus.value = DraftActionStatus.Success
        }
    }

    fun removeDraft(id: Int) {
        viewModelScope.launch {
            deleteDraftUseCase(id)
        }
    }

    fun publish(draft: TaskDraftEntity) {
        viewModelScope.launch {
            _actionStatus.value = DraftActionStatus.Loading
            publishDraftUseCase(draft)
                .onSuccess {
                    _actionStatus.value = DraftActionStatus.Success
                }
                .onFailure { exception ->
                    _actionStatus.value = DraftActionStatus.Error(
                        exception.localizedMessage ?: "Error de red al publicar. Conservando borrador."
                    )
                }
        }
    }

    fun clearActionStatus() {
        _actionStatus.value = DraftActionStatus.Idle
    }
}
