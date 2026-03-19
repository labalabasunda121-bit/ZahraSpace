package com.zahra.space.ui.screens.onboarding

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(onComplete: (User) -> Unit) {
    var step by remember { mutableIntStateOf(1) }
    var name by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var avatar by remember { mutableStateOf("👩") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (step) {
            1 -> {
                Text(
                    text = "🌸 ASSALAMU'ALAIKUM",
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Biar aplikasi ini kenal kamu,\nisi data diri kamu dulu, ya.",
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(48.dp))
                Button(
                    onClick = { step = 2 },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("LANJUT")
                }
            }
            
            2 -> {
                Text(
                    text = "Siapa nama panggilan kamu?",
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Zahra") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { step = 3 },
                    enabled = name.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("LANJUT")
                }
            }
            
            3 -> {
                Text(
                    text = "Kapan kamu lahir? (DD/MM/YYYY)",
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = birthDate,
                    onValueChange = { birthDate = it },
                    placeholder = { Text("27/09/1999") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { step = 4 },
                    enabled = birthDate.length == 10,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("LANJUT")
                }
            }
            
            4 -> {
                Text(
                    text = "Pilih avatar favoritmu:",
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("👧", "👩", "🧕").forEach { a ->
                        Card(
                            onClick = { avatar = a },
                            colors = CardDefaults.cardColors(
                                containerColor = if (avatar == a)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Text(
                                text = a,
                                fontSize = 48.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        val date = try {
                            SimpleDateFormat("dd/MM/yyyy", Locale("id")).parse(birthDate) ?: Date()
                        } catch (e: Exception) {
                            Date()
                        }
                        onComplete(
                            User(
                                name = name,
                                birthDate = date.time,
                                avatar = avatar,
                                installDate = System.currentTimeMillis()
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("SELESAI")
                }
            }
        }
    }
}
