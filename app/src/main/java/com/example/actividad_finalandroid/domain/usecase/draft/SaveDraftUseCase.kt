package com.example.actividad_finalandroid.domain.usecase.draft

import com.example.actividad_finalandroid.data.local.entity.TaskDraftEntity
import com.example.actividad_finalandroid.domain.repository.DraftRepository

class SaveDraftUseCase(
    private val repository: DraftRepository
) {
    suspend operator fun invoke(draft: TaskDraftEntity): Long {
        return repository.saveDraft(draft)
    }
}
