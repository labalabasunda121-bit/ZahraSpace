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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DzikirHomeScreen(onNavigateToCounter: (Long) -> Unit) {
    val vm: DzikirViewModel = viewModel()
    val dzikirList by vm.dzikirList.collectAsState()
    
    LaunchedEffect(Unit) {
        vm.loadDzikir()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dzikir & Do'a") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(dzikirList) { dzikir ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigateToCounter(dzikir.id) }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = dzikir.arabicText,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = dzikir.translation,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(
                            text = "${dzikir.count}×",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
