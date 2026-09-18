package com.example.actividad_finalandroid.domain.usecase.task

import com.example.actividad_finalandroid.domain.model.Task
import com.example.actividad_finalandroid.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTasksUseCase(
    private val repository: TaskRepository
) {
    operator fun invoke(ownerId: String): Flow<List<Task>> {
        return repository.getTasks(ownerId)
    }
}
