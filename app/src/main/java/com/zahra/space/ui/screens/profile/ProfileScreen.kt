package com.zahra.space.ui.screens.profile
@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zahra.space.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(onNavigateToSettings: () -> Unit, onNavigateToLetters: () -> Unit) {
    val vm: ProfileViewModel = viewModel()
    val name by vm.userName.collectAsState()
    val points by vm.totalPoints.collectAsState()
    val iman by vm.imanLevel.collectAsState()
    val join by vm.joinDate.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil") },
                actions = {
                    IconButton(onNavigateToSettings) { Text("⚙️") }
                    IconButton(onNavigateToLetters) { Text("💌") }
                }
            )
        }
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("👩", fontSize = 80.sp)
                        Text(name, style = MaterialTheme.typography.headlineMedium)
                        Text("Bergabung: $join")
                    }
                }
            }
            item {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        Text("Statistik", style = MaterialTheme.typography.titleLarge)
                        Divider(Modifier.padding(vertical = 8.dp))
                        StatRow("✨ Poin", "$points")
                        StatRow("❤️ Iman", "$iman%")
                    }
                }
            }
        }
    }
}
@Composable fun StatRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label); Text(value, color = MaterialTheme.colorScheme.primary)
    }
}
