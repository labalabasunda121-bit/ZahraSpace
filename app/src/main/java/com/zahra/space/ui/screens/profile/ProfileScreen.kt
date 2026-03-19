package com.zahra.space.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zahra.space.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToLetters: () -> Unit
) {
    val vm: ProfileViewModel = viewModel()
    val name by vm.userName.collectAsState()
    val points by vm.totalPoints.collectAsState()
    val iman by vm.imanLevel.collectAsState()
    val streak by vm.streak.collectAsState()
    val join by vm.joinDate.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Text("⚙️")
                    }
                    IconButton(onClick = onNavigateToLetters) {
                        Text("💌")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "👩",
                            fontSize = 80.sp
                        )
                        Text(
                            text = name,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = "Bergabung: $join"
                        )
                    }
                }
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Statistik",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        
                        StatRow("✨ Poin", "$points")
                        StatRow("❤️ Iman", "$iman%")
                        StatRow("🔥 Streak", "$streak hari")
                    }
                }
            }
        }
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(
            text = value,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
