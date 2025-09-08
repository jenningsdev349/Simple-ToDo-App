package com.jenningsdev.todoapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jenningsdev.todoapp.data.model.Task
import com.jenningsdev.todoapp.data.repository.local.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    val tasks = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addTask(task: Task) = viewModelScope.launch { repository.insert(task) }
    fun updateTask(task: Task) = viewModelScope.launch { repository.update(task) }
    fun deleteTask(task: Task) = viewModelScope.launch { repository.delete(task) }

    companion object {
        class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
                    return TaskViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}