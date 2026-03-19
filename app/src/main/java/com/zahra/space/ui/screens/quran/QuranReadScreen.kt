package com.zahra.space.ui.screens.quran

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zahra.space.viewmodel.QuranViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranReadScreen(surahId: Int, verseId: Int) {
    val vm: QuranViewModel = viewModel()
    val ayat by vm.currentAyat.collectAsState()
    val surahName by vm.currentSurahName.collectAsState()
    
    LaunchedEffect(surahId, verseId) {
        vm.loadAyat(surahId, verseId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$surahName - Ayat $verseId") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                        text = ayat?.ayahText ?: "",
                        fontSize = 28.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    Text(
                        text = ayat?.readText ?: "",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = ayat?.indoText ?: "",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
