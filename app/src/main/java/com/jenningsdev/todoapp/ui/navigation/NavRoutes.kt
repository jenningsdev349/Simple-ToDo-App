package com.jenningsdev.todoapp.ui.navigation

sealed class NavRoutes(val route: String) {
    data object TaskList : NavRoutes("task_list")
    data object AddTask : NavRoutes("add_task")
}
