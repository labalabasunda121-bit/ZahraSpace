package com.zahra.space.ui.screens.todo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TodoHomeScreen(
    onNavigateToDetail: (Long) -> Unit = {},
    onNavigateToCreate: () -> Unit = {}
) {
    val todos = remember {
        listOf(
            Todo(1, "Hafal Juz 30", "Quran", 30, 60f),
            Todo(2, "Olahraga Pagi", "Fitness", 21, 30f),
            Todo(3, "Baca Buku", "Self Development", 7, 100f)
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Target & Todo") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCreate) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Todo")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(todos) { todo ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigateToDetail(todo.id) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(todo.title, style = MaterialTheme.typography.titleMedium)
                            Text(
                                todo.category,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "Target: ${todo.days} hari",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        LinearProgressIndicator(
                            progress = todo.progress / 100f,
                            modifier = Modifier.width(60.dp)
                        )
                    }
                }
            }
        }
    }
}

data class Todo(
    val id: Long,
    val title: String,
    val category: String,
    val days: Int,
    val progress: Float
)

@Composable
fun TodoDetailScreen(todoId: Long) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Todo Detail") }) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text("Detail Todo ID: $todoId", modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun TodoCreateScreen(
    onSave: () -> Unit = {}
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Buat Todo Baru") }) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text("Form Create Todo", modifier = Modifier.padding(16.dp))
        }
    }
}
