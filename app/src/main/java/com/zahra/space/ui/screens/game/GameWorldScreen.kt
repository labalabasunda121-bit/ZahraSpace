package com.zahra.space.ui.screens.game

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.zahra.space.ui.views.GameWorldView

@Composable
fun GameWorldScreen() {
    var balance by remember { mutableIntStateOf(1000) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    Box(Modifier.fillMaxSize()) {
        AndroidView(factory = { ctx -> GameWorldView(ctx) }, Modifier.fillMaxSize())
        Column(Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Card(Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("🏙️ Kota Zahra")
                    Text("💰 $balance")
                }
            }
            Spacer(Modifier.weight(1f))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button({ /* move */ }) { Text("←") }
                Button({ /* move */ }) { Text("↑") }
                Button({ /* move */ }) { Text("→") }
                Button({ /* move */ }) { Text("↓") }
            }
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button({ dialogMessage = "Halo!"; showDialog = true }) { Text("💬 Ngobrol") }
                Button({ dialogMessage = "Masjid"; showDialog = true }) { Text("🕌 Masjid") }
                Button({ dialogMessage = "Restoran"; showDialog = true }) { Text("🍳 Resto") }
            }
            if (showDialog) {
                Spacer(Modifier.height(8.dp))
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(dialogMessage)
                        Button({ showDialog = false }, Modifier.align(Alignment.End)) { Text("Tutup") }
                    }
                }
            }
        }
    }
}
