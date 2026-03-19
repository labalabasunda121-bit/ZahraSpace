package com.zahra.space.ui.screens.game

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.zahra.space.ui.views.GameWorldView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameWorldScreen() {
    var balance by remember { mutableIntStateOf(1000) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx -> GameWorldView(ctx) },
            modifier = Modifier.fillMaxSize()
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("🏙️ Kota Zahra")
                    Text("💰 $balance")
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button({ }) { Text("←") }
                Button({ }) { Text("↑") }
                Button({ }) { Text("→") }
                Button({ }) { Text("↓") }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button({
                    dialogMessage = "Halo!"
                    showDialog = true
                }) {
                    Text("💬 Ngobrol")
                }
                Button({
                    dialogMessage = "Masjid"
                    showDialog = true
                }) {
                    Text("🕌 Masjid")
                }
                Button({
                    dialogMessage = "Restoran"
                    showDialog = true
                }) {
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
}
