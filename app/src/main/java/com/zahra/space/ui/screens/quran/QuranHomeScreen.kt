package com.zahra.space.ui.screens.quran
@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zahra.space.viewmodel.QuranViewModel

@Composable
fun QuranHomeScreen(
    navController: NavController,
    onNavigateToRead: (Int, Int) -> Unit,
    onNavigateToHafalan: (Int) -> Unit
) {
    val vm: QuranViewModel = viewModel()
    val surahList by vm.surahList.collectAsState()
    LaunchedEffect(Unit) { vm.loadSurahList() }
    Scaffold(topBar = { TopAppBar(title = { Text("Al-Qur'an") }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            items(surahList) { surah ->
                Card(Modifier.fillMaxWidth().padding(vertical = 4.dp), onClick = { onNavigateToRead(surah.suraId, 1) }) {
                    Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("${surah.suraId}. ${surah.surahNameLatin}", style = MaterialTheme.typography.titleMedium)
                            Text("${surah.surahName}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                        }
                        // Hafalan icon bisa ditambahkan nanti
                    }
                }
            }
        }
    }
}
