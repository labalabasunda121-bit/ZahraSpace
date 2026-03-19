package com.zahra.space.ui.screens.onboarding

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zahra.space.data.entity.User
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(onComplete: (User) -> Unit) {
    val context = LocalContext.current
    var step by remember { mutableIntStateOf(1) }
    var name by remember { mutableStateOf("") }
    var selectedAvatar by remember { mutableStateOf("👩") }
    var selectedDate by remember { mutableStateOf<Date?>(null) }
    
    val avatars = listOf("👧", "👩", "🧕")
    val calendar = Calendar.getInstance()
    
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
                    text = "Kapan kamu lahir?",
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = {
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                calendar.set(year, month, dayOfMonth)
                                selectedDate = calendar.time
                                step = 4
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (selectedDate == null) "Pilih Tanggal" else "✓ Tanggal Dipilih")
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = { step = 2 },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("KEMBALI")
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
                    avatars.forEach { avatar ->
                        Card(
                            onClick = { selectedAvatar = avatar },
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedAvatar == avatar)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Text(
                                text = avatar,
                                fontSize = 48.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = {
                        try {
                            val user = User(
                                name = name,
                                birthDate = selectedDate?.time ?: System.currentTimeMillis(),
                                avatar = selectedAvatar,
                                installDate = System.currentTimeMillis()
                            )
                            onComplete(user)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("SELESAI")
                }
            }
        }
    }
}
