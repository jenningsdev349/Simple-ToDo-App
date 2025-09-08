package com.jenningsdev.todoapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jenningsdev.todoapp.R
import com.jenningsdev.todoapp.data.model.Task
import com.jenningsdev.todoapp.ui.navigation.NavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(navController: NavController, viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarColors(
                    containerColor = colorResource(R.color.blue_500),
                    scrolledContainerColor = colorResource(R.color.white),
                    navigationIconContentColor = colorResource(R.color.white),
                    titleContentColor = colorResource(R.color.white),
                    actionIconContentColor = colorResource(R.color.white)
                ),
                title = { Text(stringResource(R.string.task_list_app_bar_text)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_content_description)
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(NavRoutes.AddTask.route) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_task_button_content_description)
                )
            }
        }
    ) { padding ->
        if (tasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.task_list_no_tasks_text),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                contentPadding = padding,
                modifier = Modifier.fillMaxSize()
            ) {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onToggleComplete = {
                            viewModel.updateTask(task.copy(isCompleted = !task.isCompleted))
                        },
                        onDelete = {
                            viewModel.deleteTask(task)
                            Toast.makeText(
                                context,
                                context.getString(R.string.toast_task_deleted),
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onUpdate = { updatedTask ->
                            viewModel.updateTask(updatedTask)
                            Toast.makeText(
                                context,
                                context.getString(R.string.toast_task_edited),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit,
    onUpdate: (Task) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showEditDialog = true }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onToggleComplete() }
                )
                Column {
                    Text(task.title, style = MaterialTheme.typography.titleMedium)
                    Text(task.category, style = MaterialTheme.typography.bodySmall)
                }
            }

            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_button_content_description),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_task_dialog_title)) },
            text = { Text(stringResource(R.string.delete_task_dialog_description)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    }
                ) {
                    Text(
                        stringResource(R.string.delete_task_dialog_positive_action),
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.delete_task_dialog_negative_action))
                }
            }
        )
    }

    if (showEditDialog) {
        var updatedTitle by remember { mutableStateOf(task.title) }
        var updatedDescription by remember { mutableStateOf(task.description) }
        var updatedCategory by remember { mutableStateOf(task.category) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text(stringResource(R.string.edit_task_dialog_title)) },
            text = {
                Column {
                    OutlinedTextField(
                        value = updatedTitle,
                        onValueChange = { updatedTitle = it },
                        label = { Text(stringResource(R.string.task_title_label)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = updatedDescription,
                        onValueChange = { updatedDescription = it },
                        label = { Text(stringResource(R.string.task_description_label)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp),
                        singleLine = false,
                        maxLines = 5
                    )
                    OutlinedTextField(
                        value = updatedCategory,
                        onValueChange = { updatedCategory = it },
                        label = { Text(stringResource(R.string.task_category_label)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showEditDialog = false
                        onUpdate(
                            task.copy(
                                title = updatedTitle,
                                description = updatedDescription,
                                category = updatedCategory
                            )
                        )
                    }
                ) {
                    Text(stringResource(R.string.save_button))
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }
}
