package com.jenningsdev.todoapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jenningsdev.todoapp.R
import com.jenningsdev.todoapp.data.model.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(navController: NavController, viewModel: TaskViewModel) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Task") },
                colors = TopAppBarColors(
                    containerColor = colorResource(R.color.blue_700),
                    scrolledContainerColor = colorResource(R.color.white),
                    navigationIconContentColor = colorResource(R.color.white),
                    titleContentColor = colorResource(R.color.white),
                    actionIconContentColor = colorResource(R.color.white)
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_content_description)
                        )
                    }
                }

            )
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding).padding(16.dp)) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(R.string.task_title_label)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.task_description_label)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text(stringResource(R.string.task_category_label)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))
            Button(
                colors = ButtonColors(
                    containerColor = colorResource(R.color.purple_300),
                    contentColor = colorResource(R.color.white),
                    disabledContainerColor = colorResource(R.color.purple_100),
                    disabledContentColor = colorResource(R.color.white)
                ),
                onClick = {
                    viewModel.addTask(
                        Task(
                            title = title,
                            description = description,
                            category = category)
                    )
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_task_added),
                        Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.add_task_title))
            }
        }
    }
}
