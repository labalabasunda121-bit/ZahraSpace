package com.zahra.space.ui.screens.checklist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChecklistScreen() {
    val checklistItems = remember {
        mutableStateListOf(
            ChecklistItem("Sholat Subuh", false),
            ChecklistItem("Sholat Dzuhur", false),
            ChecklistItem("Sholat Ashar", false),
            ChecklistItem("Sholat Maghrib", false),
            ChecklistItem("Sholat Isya", false),
            ChecklistItem("Sholat Dhuha", false),
            ChecklistItem("Dzikir Pagi", false),
            ChecklistItem("Dzikir Petang", false),
            ChecklistItem("Baca Quran", false),
            ChecklistItem("Sedekah", false)
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Checklist") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(checklistItems) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(item.name, style = MaterialTheme.typography.bodyLarge)
                        Checkbox(
                            checked = item.isChecked,
                            onCheckedChange = { 
                                item.isChecked = !item.isChecked
                            }
                        )
                    }
                }
            }
        }
    }
}

data class ChecklistItem(
    val name: String,
    var isChecked: Boolean
)
