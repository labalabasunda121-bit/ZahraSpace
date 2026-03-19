package com.zahra.space.ui.screens.splash
@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    var state by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        delay(500); state = 1; delay(2000); state = 2; delay(2000); state = 3; delay(2000); onTimeout()
    }
    Box(Modifier.fillMaxSize().background(Color(0xFF0A1929)), contentAlignment = Alignment.Center) {
        when (state) {
            1 -> Text("Assalamu'alaikum", color = Color.White, fontSize = 24.sp)
            2 -> Text("Selamat datang", color = Color.White, fontSize = 24.sp)
            3 -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🌙", fontSize = 80.sp)
                Text("ZAHRASPACE", color = Color.White, fontSize = 28.sp, letterSpacing = 4.sp)
                Text("created by Fajar", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
                Text("for Zahra", color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}
