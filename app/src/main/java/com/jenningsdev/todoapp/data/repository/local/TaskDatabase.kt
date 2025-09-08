package com.jenningsdev.todoapp.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jenningsdev.todoapp.data.model.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}