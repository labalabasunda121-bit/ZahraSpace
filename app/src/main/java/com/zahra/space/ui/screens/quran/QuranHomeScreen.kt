package com.zahra.space.ui.screens.quran
@file:Suppress("ExperimentalMaterial3Api")

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun QuranHomeScreen(
    navController: NavController,
    onNavigateToRead: (Int, Int) -> Unit,
    onNavigateToHafalan: (Int) -> Unit
) {
    val surahList = remember {
        listOf(
            Surah(1, "Al-Fatihah", "الفاتحة", 7, "Makkiyah"),
            Surah(2, "Al-Baqarah", "البقرة", 286, "Madaniyyah"),
            Surah(3, "Ali 'Imran", "آل عمران", 200, "Madaniyyah"),
            Surah(4, "An-Nisa'", "النساء", 176, "Madaniyyah"),
            Surah(5, "Al-Ma'idah", "المائدة", 120, "Madaniyyah")
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Al-Qur'an") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(surahList) { surah ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigateToRead(surah.number, 1) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "${surah.number}. ${surah.nameLatin}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = surah.nameArabic,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "${surah.ayatCount} Ayat • ${surah.type}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        IconButton(onClick = { onNavigateToHafalan(surah.number) }) {
                            Icon(Icons.Default.Bookmark, contentDescription = "Hafalan")
                        }
                    }
                }
            }
        }
    }
}

data class Surah(
    val number: Int,
    val nameLatin: String,
    val nameArabic: String,
    val ayatCount: Int,
    val type: String
)

@Composable
fun QuranReadScreen(surahId: Int, ayatNumber: Int) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Quran - Surah $surahId") }) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "Surah $surahId Ayat $ayatNumber\n\nبِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ\n\nDengan nama Allah Yang Maha Pengasih lagi Maha Penyayang",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun QuranHafalanScreen(surahId: Int) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Hafalan - Surah $surahId") }) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text("Mode Hafalan - Surah $surahId", modifier = Modifier.padding(16.dp))
        }
    }
}
