package com.example.actividad_finalandroid.domain.usecase.draft

import com.example.actividad_finalandroid.data.local.entity.TaskDraftEntity
import com.example.actividad_finalandroid.domain.repository.DraftRepository
import kotlinx.coroutines.flow.Flow

class GetDraftsUseCase(
    private val repository: DraftRepository
) {
    operator fun invoke(ownerId: String): Flow<List<TaskDraftEntity>> {
        return repository.getDrafts(ownerId)
    }
}
