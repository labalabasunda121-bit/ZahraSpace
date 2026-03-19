package com.zahra.space.ui.screens.dzikir
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zahra.space.viewmodel.DzikirViewModel

@Composable
fun DzikirHomeScreen(onNavigateToCounter: (Long) -> Unit) {
    val vm: DzikirViewModel = viewModel()
    val dzikirList by vm.dzikirList.collectAsState()
    LaunchedEffect(Unit) { vm.loadDzikir() }
    Scaffold(topBar = { TopAppBar(title = { Text("Dzikir & Do'a") }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(dzikirList) { dzikir ->
                Card(Modifier.fillMaxWidth(), onClick = { onNavigateToCounter(dzikir.id) }) {
                    Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(dzikir.arabicText, style = MaterialTheme.typography.headlineSmall)
                        Text(dzikir.translation, style = MaterialTheme.typography.bodyMedium)
                        Divider(Modifier.padding(vertical = 8.dp))
                        Text("${dzikir.count}×", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
