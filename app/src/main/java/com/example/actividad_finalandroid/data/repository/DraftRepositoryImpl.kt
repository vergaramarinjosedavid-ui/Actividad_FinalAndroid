package com.example.actividad_finalandroid.data.repository

import com.example.actividad_finalandroid.data.local.TaskDraftDao
import com.example.actividad_finalandroid.data.local.entity.TaskDraftEntity
import com.example.actividad_finalandroid.domain.repository.DraftRepository
import kotlinx.coroutines.flow.Flow

class DraftRepositoryImpl(
    private val taskDraftDao: TaskDraftDao
) : DraftRepository {

    override fun getDrafts(ownerId: String): Flow<List<TaskDraftEntity>> {
        return taskDraftDao.getDrafts(ownerId)
    }

    override suspend fun saveDraft(draft: TaskDraftEntity): Long {
        return taskDraftDao.insertDraft(draft)
    }

    override suspend fun deleteDraft(id: Int) {
        taskDraftDao.deleteDraftById(id)
    }
}
