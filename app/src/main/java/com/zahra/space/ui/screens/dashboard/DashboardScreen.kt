package com.zahra.space.ui.screens.dashboard

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

@OptIn(ExperimentalMaterial3Api::class)
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
    
    val menuItems = listOf(
        MenuItem("Quran", "📖", onNavigateToQuran),
        MenuItem("Dzikir", "📿", onNavigateToDzikir),
        MenuItem("Checklist", "✅", onNavigateToChecklist),
        MenuItem("Todo", "📋", onNavigateToTodo),
        MenuItem("Fitness", "💪", onNavigateToFitness),
        MenuItem("Pet", "🐱", onNavigateToPet),
        MenuItem("Game", "🎮", onNavigateToGame),
        MenuItem("Profile", "👤", onNavigateToProfile),
        MenuItem("Surat", "💌", onNavigateToLetters)
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "$greeting, $name!",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("❤️ Iman")
                    LinearProgressIndicator(
                        progress = iman / 100f,
                        modifier = Modifier.width(150.dp)
                    )
                }
                Text("✨ $points")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("🐱 Luna")
                    Text("Level ${pet.level}")
                }
                
                AndroidView(
                    factory = { ctx -> Luna3DView(ctx) },
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.CenterHorizontally)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ActionButton("🍖") { viewModel.feedPet() }
                    ActionButton("🎾") { viewModel.playWithPet() }
                    ActionButton("🧼") { viewModel.cleanPet() }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(menuItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    onClick = item.onClick
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = item.icon,
                            fontSize = 24.sp
                        )
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActionButton(icon: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(48.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Text(icon, fontSize = 16.sp)
    }
}

data class MenuItem(val title: String, val icon: String, val onClick: () -> Unit)
