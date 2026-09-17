package com.example.actividad_finalandroid.domain.repository

import com.example.actividad_finalandroid.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasks(ownerId: String): Flow<List<Task>>
    fun getTaskById(id: String): Flow<Task?>
    suspend fun insertTask(task: Task): Result<Unit>
    suspend fun updateTask(task: Task): Result<Unit>
    suspend fun deleteTask(id: String): Result<Unit>
}
