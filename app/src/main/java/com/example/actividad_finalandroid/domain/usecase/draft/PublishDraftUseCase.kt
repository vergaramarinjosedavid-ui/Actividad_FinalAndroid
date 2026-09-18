package com.example.actividad_finalandroid.domain.usecase.draft

import com.example.actividad_finalandroid.data.local.entity.TaskDraftEntity
import com.example.actividad_finalandroid.domain.model.Task
import com.example.actividad_finalandroid.domain.repository.DraftRepository
import com.example.actividad_finalandroid.domain.repository.TaskRepository

class PublishDraftUseCase(
    private val draftRepository: DraftRepository,
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(draft: TaskDraftEntity): Result<Unit> {
        val task = Task(
            ownerId = draft.ownerId,
            title = draft.title,
            description = draft.description
        )
        
        return taskRepository.insertTask(task).onSuccess {
            draftRepository.deleteDraft(draft.id)
        }
    }
}
