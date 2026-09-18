package com.example.actividad_finalandroid.domain.usecase.draft

import com.example.actividad_finalandroid.domain.repository.DraftRepository

class DeleteDraftUseCase(
    private val repository: DraftRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.deleteDraft(id)
    }
}
