package com.example.actividad_finalandroid.data.repository

import com.example.actividad_finalandroid.domain.model.Task
import com.example.actividad_finalandroid.domain.repository.TaskRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TaskRepositoryImpl : TaskRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val tasksCollection = firestore.collection("tasks")

    override fun getTasks(ownerId: String): Flow<List<Task>> = callbackFlow {
        val listener = tasksCollection
            .whereEqualTo("ownerId", ownerId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val taskList = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Task::class.java)?.copy(id = doc.id)
                    }
                    trySend(taskList)
                }
            }
        awaitClose { listener.remove() }
    }

    override fun getTaskById(id: String): Flow<Task?> = callbackFlow {
        val listener = tasksCollection.document(id)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val task = snapshot.toObject(Task::class.java)?.copy(id = snapshot.id)
                    trySend(task)
                } else {
                    trySend(null)
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun insertTask(task: Task): Result<Unit> {
        return try {
            val currentTime = System.currentTimeMillis()
            val docRef = if (task.id.isBlank()) tasksCollection.document() else tasksCollection.document(task.id)
            val updatedTask = task.copy(
                id = docRef.id,
                createdAt = if (task.createdAt == 0L) currentTime else task.createdAt,
                updatedAt = currentTime
            )
            docRef.set(updatedTask).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTask(task: Task): Result<Unit> {
        return try {
            val updatedTask = task.copy(updatedAt = System.currentTimeMillis())
            tasksCollection.document(task.id).set(updatedTask).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTask(id: String): Result<Unit> {
        return try {
            tasksCollection.document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
