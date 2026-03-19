package com.zahra.space.ui.screens.onboarding
@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zahra.space.data.entity.User
import java.text.SimpleDateFormat
import java.util.*
@Composable
fun OnboardingScreen(onComplete: (User) -> Unit) {
    var step by remember { mutableIntStateOf(1) }
    var name by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var avatar by remember { mutableStateOf("👩") }
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        when (step) {
            1 -> { Text("🌸 ASSALAMU'ALAIKUM", fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(32.dp))
                Text("Biar aplikasi ini kenal kamu,\nisi data diri kamu dulu, ya.", fontSize = 16.sp)
                Spacer(Modifier.height(48.dp))
                Button({ step = 2 }, Modifier.fillMaxWidth()) { Text("LANJUT") } }
            2 -> { Text("Siapa nama panggilan kamu?", fontSize = 18.sp)
                OutlinedTextField(value = name, onValueChange = { name = it }, Modifier.fillMaxWidth(), placeholder = { Text("Zahra") })
                Spacer(Modifier.height(32.dp))
                Button({ step = 3 }, enabled = name.isNotBlank(), Modifier.fillMaxWidth()) { Text("LANJUT") } }
            3 -> { Text("Kapan kamu lahir? (DD/MM/YYYY)", fontSize = 18.sp)
                OutlinedTextField(value = birthDate, onValueChange = { birthDate = it }, Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                Spacer(Modifier.height(32.dp))
                Button({ step = 4 }, enabled = birthDate.length == 10, Modifier.fillMaxWidth()) { Text("LANJUT") } }
            4 -> { Text("Pilih avatar favoritmu:", fontSize = 18.sp)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    listOf("👧", "👩", "🧕").forEach { a ->
                        Card({ avatar = a }, colors = CardDefaults.cardColors(containerColor = if (avatar == a) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)) {
                            Text(a, fontSize = 48.sp, Modifier.padding(16.dp)) } } }
                Spacer(Modifier.height(32.dp))
                Button({
                    val date = try { SimpleDateFormat("dd/MM/yyyy", Locale("id")).parse(birthDate) ?: Date() } catch (e: Exception) { Date() }
                    onComplete(User(name = name, birthDate = date.time, avatar = avatar, installDate = System.currentTimeMillis()))
                }, Modifier.fillMaxWidth()) { Text("SELESAI") } }
        }
    }
}
