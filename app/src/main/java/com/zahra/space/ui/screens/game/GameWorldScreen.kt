package com.zahra.space.ui.screens.game

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameWorldScreen() {
    var balance by remember { mutableIntStateOf(1000) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var position by remember { mutableIntStateOf(0) }
    
    val npcMessages = listOf(
        "Assalamu'alaikum, Zahra!",
        "Jangan lupa sholat ya...",
        "Hari ini cerah sekali.",
        "Kucingmu lucu sekali.",
        "Doaku selalu untukmu."
    )
    
    val hiddenMessages = listOf(
        "Jangan lupa bahagia, walau jauh.",
        "Ada yang titip: 'Jagalah hatimu.'",
        "Setiap senja, aku mendoakanmu.",
        "Aku di sini, menjaga hatimu.",
        "-F"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("🏙️ KOTA ZAHRA", style = MaterialTheme.typography.titleMedium)
                Text("💰 $balance", style = MaterialTheme.typography.titleMedium)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Game Area (2D representation)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📍 POSISI: $position", fontSize = 20.sp)
                    Text("🏙️", fontSize = 80.sp)
                    Text("KOTA ZAHRA", fontSize = 16.sp)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { position -= 1 }
            ) {
                Text("←")
            }
            Button(
                onClick = { position += 1 }
            ) {
                Text("→")
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    dialogMessage = npcMessages.random()
                    showDialog = true
                }
            ) {
                Text("💬 Ngobrol")
            }
            Button(
                onClick = {
                    dialogMessage = "Masjid - Tempat ibadah"
                    showDialog = true
                }
            ) {
                Text("🕌 Masjid")
            }
            Button(
                onClick = {
                    dialogMessage = "Restoran - Ayo masak!"
                    showDialog = true
                }
            ) {
                Text("🍳 Resto")
            }
        }
        
        if (showDialog) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(dialogMessage)
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = { showDialog = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Tutup")
                    }
                }
            }
        }
    }
}
