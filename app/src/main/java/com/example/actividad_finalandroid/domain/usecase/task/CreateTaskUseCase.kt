package com.example.actividad_finalandroid.domain.usecase.task

import com.example.actividad_finalandroid.domain.model.Task
import com.example.actividad_finalandroid.domain.repository.TaskRepository

class CreateTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task): Result<Unit> {
        return repository.insertTask(task)
    }
}
