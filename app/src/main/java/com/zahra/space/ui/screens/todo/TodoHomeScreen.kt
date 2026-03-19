package com.zahra.space.ui.screens.todo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zahra.space.viewmodel.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoHomeScreen(
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToCreate: () -> Unit
) {
    val vm: TodoViewModel = viewModel()
    val todos by vm.activeTodos.collectAsState()
    
    LaunchedEffect(Unit) {
        vm.loadActiveTodos()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Target & Todo") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreate
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
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
                            Text(
                                text = todo.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = todo.category,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        LinearProgressIndicator(
                            progress = (todo.currentAmount?.toFloat() ?: 0f) / (todo.targetAmount?.toFloat() ?: 1f),
                            modifier = Modifier.width(60.dp)
                        )
                    }
                }
            }
        }
    }
}
