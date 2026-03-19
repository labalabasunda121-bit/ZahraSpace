package com.zahra.space.ui.screens.letters

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zahra.space.viewmodel.LetterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyLetterScreen() {
    val vm: LetterViewModel = viewModel()
    val letters by vm.letters.collectAsState()
    
    LaunchedEffect(Unit) {
        vm.loadLetters()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Surat Bulanan") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(letters) { letter ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = letter.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = letter.content,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
