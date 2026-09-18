package com.example.actividad_finalandroid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.actividad_finalandroid.data.local.entity.TaskDraftEntity

@Database(entities = [TaskDraftEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDraftDao(): TaskDraftDao
}
