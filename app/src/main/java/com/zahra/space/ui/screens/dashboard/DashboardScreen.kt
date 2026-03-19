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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zahra.space.viewmodel.DashboardViewModel

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
    val streak by viewModel.streak.collectAsState()
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
        // Header
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
        
        // Stats Card
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
                Column {
                    Text("❤️ Iman", style = MaterialTheme.typography.titleMedium)
                    LinearProgressIndicator(
                        progress = iman / 100f,
                        modifier = Modifier.width(150.dp)
                    )
                }
                Text("✨ $points", style = MaterialTheme.typography.titleMedium)
                Text("🔥 $streak", style = MaterialTheme.typography.titleMedium)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Prayer Checklist
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("✅ Sholat Hari Ini", style = MaterialTheme.typography.titleMedium)
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PrayerButton("S", "Subuh")
                    PrayerButton("D", "Dzuhur")
                    PrayerButton("A", "Ashar")
                    PrayerButton("M", "Maghrib")
                    PrayerButton("I", "Isya")
                    PrayerButton("Dh", "Dhuha")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Luna Card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🐱 Luna", style = MaterialTheme.typography.titleMedium)
                    Text("Level ${pet.level}", style = MaterialTheme.typography.bodyMedium)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Luna ASCII Art
                Text(
                    text = "   /\\_/\\\n  ( o.o )\n   > ^ <",
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Status Bars
                StatBar("🍖 Lapar", pet.hunger)
                StatBar("😊 Senang", pet.happiness)
                StatBar("🧼 Bersih", pet.cleanliness)
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ActionButton("🍖") { viewModel.feedPet() }
                    ActionButton("🎾") { viewModel.playWithPet() }
                    ActionButton("🧼") { viewModel.cleanPet() }
                    ActionButton("💤") { viewModel.sleepPet() }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Menu Grid
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
fun PrayerButton(text: String, description: String) {
    var checked by remember { mutableStateOf(false) }
    Button(
        onClick = { checked = !checked },
        modifier = Modifier.size(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (checked) 
                MaterialTheme.colorScheme.primary 
            else 
                MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Text(text, fontSize = 12.sp)
    }
}

@Composable
fun StatBar(label: String, value: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, modifier = Modifier.width(60.dp))
        LinearProgressIndicator(
            progress = value / 100f,
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
        )
        Text("$value%", modifier = Modifier.width(40.dp))
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
