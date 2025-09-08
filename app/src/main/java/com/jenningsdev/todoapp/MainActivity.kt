package com.jenningsdev.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.jenningsdev.todoapp.data.repository.local.TaskDatabase
import com.jenningsdev.todoapp.data.repository.local.TaskRepository
import com.jenningsdev.todoapp.ui.navigation.NavRoutes
import com.jenningsdev.todoapp.ui.screens.AddTaskScreen
import com.jenningsdev.todoapp.ui.screens.TaskListScreen
import com.jenningsdev.todoapp.ui.screens.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(applicationContext, TaskDatabase::class.java, "task_db").build()
        val repository = TaskRepository(db.taskDao())

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val taskViewModel: TaskViewModel = viewModel(factory = TaskViewModel.Companion.TaskViewModelFactory(repository))
                NavHost(navController, startDestination = NavRoutes.TaskList.route) {
                    composable(NavRoutes.TaskList.route) {
                        TaskListScreen(navController, taskViewModel)
                    }
                    composable(NavRoutes.AddTask.route) {
                        AddTaskScreen(navController, taskViewModel)
                    }
                }
            }
        }
    }
}
