package com.zahra.space.ui.screens.letters
@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zahra.space.viewmodel.LetterViewModel

@Composable
fun MonthlyLetterScreen() {
    val vm: LetterViewModel = viewModel()
    val letters by vm.letters.collectAsState()
    LaunchedEffect(Unit) { vm.loadLetters() }
    Scaffold(topBar = { TopAppBar(title = { Text("Surat Bulanan") }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            items(letters) { letter ->
                Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(letter.title, style = MaterialTheme.typography.titleMedium)
                        Text(letter.content, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
