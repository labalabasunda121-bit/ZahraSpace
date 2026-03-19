package com.zahra.space.ui.screens.fitness
@file:Suppress("ExperimentalMaterial3Api")

import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment
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
fun FitnessHomeScreen(
    onNavigateToTarget: (Long) -> Unit = {},
    onNavigateToLog: () -> Unit = {}
) {
    val targets = remember {
        listOf(
            FitnessTarget(1, "Weight Loss", "5 kg", 40f),
            FitnessTarget(2, "Push-up", "100×", 60f),
            FitnessTarget(3, "Running", "42 km", 25f)
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fitness Tracker") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToLog) {
                Icon(Icons.Default.Add, contentDescription = "Log Activity")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Today's Progress", style = MaterialTheme.typography.titleMedium)
                        LinearProgressIndicator(
                            progress = 0.7f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text("Steps: 6.543")
                            Text("Cal: 2.100")
                        }
                    }
                }
            }
            
            items(targets) { target ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigateToTarget(target.id) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(target.name, style = MaterialTheme.typography.titleMedium)
                            Text(
                                "Target: ${target.target}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        LinearProgressIndicator(
                            progress = target.progress / 100f,
                            modifier = Modifier.width(60.dp)
                        )
                    }
                }
            }
        }
    }
}

data class FitnessTarget(
    val id: Long,
    val name: String,
    val target: String,
    val progress: Float
)

@Composable
fun FitnessTargetScreen(targetId: Long) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Fitness Target") }) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text("Target ID: $targetId", modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun FitnessLogScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Log Activity") }) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text("Form Log Activity", modifier = Modifier.padding(16.dp))
        }
    }
}
