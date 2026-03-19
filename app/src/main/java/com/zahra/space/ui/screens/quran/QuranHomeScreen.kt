package com.zahra.space.ui.screens.quran

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranHomeScreen(
    navController: NavController,
    onNavigateToRead: (Int, Int) -> Unit,
    onNavigateToHafalan: (Int) -> Unit
) {
    val vm: QuranViewModel = viewModel()
    val surahList by vm.surahList.collectAsState()
    val surahNames by vm.surahNames.collectAsState()
    
    LaunchedEffect(Unit) {
        vm.loadSurahList()
        vm.loadSurahNames()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Al-Qur'an") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(surahList) { surah ->
                val name = surahNames[surah.suraId] ?: SurahInfo("Surah ${surah.suraId}", "")
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigateToRead(surah.suraId, 1) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "${surah.suraId}. ${name.latin}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = name.arabic,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
