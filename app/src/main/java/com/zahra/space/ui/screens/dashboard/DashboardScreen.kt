package com.zahra.space.ui.screens.dashboard
@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zahra.space.ui.views.Luna3DView
import com.zahra.space.viewmodel.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.*

data class MenuItem(val title: String, val icon: String, val onClick: () -> Unit)

@Composable
fun DashboardScreen(
    onNavigateToQuran: () -> Unit,
    onNavigateToDzikir: () -> Unit,
    onNavigateToChecklist: () -> Unit,
    onNavigateToTodo: () -> Unit,
    onNavigateToFitness: () -> Unit,
    onNavigateToPet: () -> Unit,
    onNavigateToGame: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToLetters: () -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {
    val name by viewModel.userName.collectAsState()
    val points by viewModel.totalPoints.collectAsState()
    val iman by viewModel.imanLevel.collectAsState()
    val pet by viewModel.petStatus.collectAsState()
    val greeting by viewModel.greeting.collectAsState()
    val date by viewModel.currentDate.collectAsState()
    val prayerSubuh by viewModel.prayerSubuh.collectAsState()
    // etc...

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("$greeting, $name!", style = MaterialTheme.typography.headlineSmall)
                Text(date, style = MaterialTheme.typography.bodyMedium)
            }
        }
        Spacer(Modifier.height(16.dp))
        Card(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Column { Text("❤️ Iman"); LinearProgressIndicator(iman / 100f, Modifier.width(150.dp)) }
                Text("✨ $points")
            }
        }
        Spacer(Modifier.height(16.dp))
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("🐱 Luna")
                    Text("Level ${pet.level}")
                }
                AndroidView(factory = { ctx -> Luna3DView(ctx) }, Modifier.size(100.dp).align(Alignment.CenterHorizontally))
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    ActionButton("🍖") { viewModel.feedPet() }
                    ActionButton("🎾") { viewModel.playWithPet() }
                    ActionButton("🧼") { viewModel.cleanPet() }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(2), Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listOf(
                MenuItem("Quran", "📖", onNavigateToQuran),
                MenuItem("Dzikir", "📿", onNavigateToDzikir),
                MenuItem("Checklist", "✅", onNavigateToChecklist),
                MenuItem("Todo", "📋", onNavigateToTodo),
                MenuItem("Fitness", "💪", onNavigateToFitness),
                MenuItem("Pet", "🐱", onNavigateToPet),
                MenuItem("Game", "🎮", onNavigateToGame),
                MenuItem("Profile", "👤", onNavigateToProfile),
                MenuItem("Surat", "💌", onNavigateToLetters)
            )) { item ->
                Card(Modifier.fillMaxWidth().aspectRatio(1f), onClick = item.onClick) {
                    Column(Modifier.fillMaxSize().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Text(item.icon, fontSize = 24.sp)
                        Text(item.title, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable fun ActionButton(icon: String, onClick: () -> Unit) {
    Button(onClick, Modifier.size(48.dp), shape = MaterialTheme.shapes.small) { Text(icon, fontSize = 16.sp) }
}
data class MenuItem(val title: String, val icon: String, val onClick: () -> Unit)
