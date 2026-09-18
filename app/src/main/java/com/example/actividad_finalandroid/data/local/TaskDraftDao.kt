package com.example.actividad_finalandroid.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.actividad_finalandroid.data.local.entity.TaskDraftEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDraftDao {
    @Query("SELECT * FROM task_drafts WHERE ownerId = :ownerId ORDER BY savedAt DESC")
    fun getDrafts(ownerId: String): Flow<List<TaskDraftEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraft(draft: TaskDraftEntity): Long

    @Query("DELETE FROM task_drafts WHERE id = :id")
    suspend fun deleteDraftById(id: Int)
}
