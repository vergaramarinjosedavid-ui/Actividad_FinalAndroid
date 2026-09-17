package com.example.actividad_finalandroid.domain.repository

import com.example.actividad_finalandroid.data.local.entity.TaskDraftEntity
import kotlinx.coroutines.flow.Flow

interface DraftRepository {
    fun getDrafts(ownerId: String): Flow<List<TaskDraftEntity>>
    suspend fun saveDraft(draft: TaskDraftEntity): Long
    suspend fun deleteDraft(id: Int)
}
