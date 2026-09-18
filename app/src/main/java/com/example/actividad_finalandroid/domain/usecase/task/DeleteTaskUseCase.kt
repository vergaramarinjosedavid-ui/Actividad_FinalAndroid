package com.example.actividad_finalandroid.domain.usecase.task

import com.example.actividad_finalandroid.domain.repository.TaskRepository

class DeleteTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return repository.deleteTask(id)
    }
}
