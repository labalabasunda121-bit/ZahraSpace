package com.zahra.space.ui.screens.checklist
@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ChecklistItem(val name: String, var isChecked: Boolean)

@Composable
fun ChecklistScreen() {
    val items = remember { mutableStateListOf(
        ChecklistItem("Sholat Subuh", false),
        ChecklistItem("Sholat Dzuhur", false),
        ChecklistItem("Sholat Ashar", false),
        ChecklistItem("Sholat Maghrib", false),
        ChecklistItem("Sholat Isya", false),
        ChecklistItem("Dzikir Pagi", false),
        ChecklistItem("Dzikir Petang", false),
        ChecklistItem("Baca Quran", false)
    ) }
    Scaffold(topBar = { TopAppBar(title = { Text("Daily Checklist") }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items) { item ->
                Card(Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(item.name)
                        Checkbox(item.isChecked, { item.isChecked = it })
                    }
                }
            }
        }
    }
}
