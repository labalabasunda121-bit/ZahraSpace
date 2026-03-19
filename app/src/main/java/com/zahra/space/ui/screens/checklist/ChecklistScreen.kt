package com.zahra.space.ui.screens.checklist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistScreen() {
    val items = remember {
        mutableStateListOf(
            ChecklistItem("Sholat Subuh", false),
            ChecklistItem("Sholat Dzuhur", false),
            ChecklistItem("Sholat Ashar", false),
            ChecklistItem("Sholat Maghrib", false),
            ChecklistItem("Sholat Isya", false),
            ChecklistItem("Dzikir Pagi", false),
            ChecklistItem("Dzikir Petang", false),
            ChecklistItem("Baca Quran", false)
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Checklist") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
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
                        Text(item.name)
                        Checkbox(
                            checked = item.isChecked,
                            onCheckedChange = { item.isChecked = it }
                        )
                    }
                }
            }
        }
    }
}

data class ChecklistItem(val name: String, var isChecked: Boolean)
