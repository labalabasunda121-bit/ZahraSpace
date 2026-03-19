package com.zahra.space.ui.screens.dzikir
@file:Suppress("ExperimentalMaterial3Api")

import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DzikirHomeScreen(
    onNavigateToCounter: (Long) -> Unit = {}
) {
    val dzikirList = remember {
        listOf(
            Dzikir(1, "سُبْحَانَ اللَّهِ", "Maha Suci Allah", 33, "Tasbih"),
            Dzikir(2, "الْحَمْدُ لِلَّهِ", "Segala puji bagi Allah", 33, "Tahmid"),
            Dzikir(3, "اللَّهُ أَكْبَرُ", "Allah Maha Besar", 33, "Takbir"),
            Dzikir(4, "أَسْتَغْفِرُ اللَّهَ", "Aku memohon ampun", 100, "Istigfar")
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dzikir & Do'a") },
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${dzikir.count}×", style = MaterialTheme.typography.labelMedium)
                            Text(
                                dzikir.category,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

data class Dzikir(
    val id: Long,
    val arabicText: String,
    val translation: String,
    val count: Int,
    val category: String
)

@Composable
fun DzikirCounterScreen(dzikirId: Long) {
    var count by remember { mutableIntStateOf(0) }
    
    Scaffold(
        topBar = { TopAppBar(title = { Text("Dzikir Counter") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = count.toString(),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { count++ },
                modifier = Modifier.size(100.dp)
            ) {
                Text("TAP", fontSize = 20.sp)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = { count = 0 },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Reset")
            }
        }
    }
}
