package com.zahra.space.ui.screens.quran
@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zahra.space.viewmodel.QuranViewModel

@Composable
fun QuranReadScreen(surahId: Int, verseId: Int) {
    val vm: QuranViewModel = viewModel()
    val ayat by vm.currentAyat.collectAsState()
    val surahName by vm.surahName.collectAsState()
    LaunchedEffect(surahId, verseId) { vm.loadAyat(surahId, verseId) }
    Scaffold(topBar = { TopAppBar(title = { Text("$surahName - Ayat $verseId") }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(ayat?.ayahText ?: "", fontSize = 28.sp, modifier = Modifier.padding(bottom = 16.dp))
                    Divider(Modifier.padding(vertical = 8.dp))
                    Text(ayat?.readText ?: "", fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
                    Text(ayat?.indoText ?: "", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                }
            }
        }
    }
}
