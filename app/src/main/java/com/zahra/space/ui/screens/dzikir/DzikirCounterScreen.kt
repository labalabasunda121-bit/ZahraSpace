package com.zahra.space.ui.screens.dzikir
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DzikirCounterScreen(dzikirId: Long) {
    var count by remember { mutableIntStateOf(0) }
    Scaffold(topBar = { TopAppBar(title = { Text("Dzikir Counter") }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(count.toString(), fontSize = 48.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Button({ count++ }, Modifier.size(100.dp)) { Text("TAP", fontSize = 20.sp) }
            Spacer(Modifier.height(8.dp))
            Button({ count = 0 }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)) { Text("Reset") }
        }
    }
}
