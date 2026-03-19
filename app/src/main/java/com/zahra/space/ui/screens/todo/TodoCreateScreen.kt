package com.zahra.space.ui.screens.todo
@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TodoCreateScreen(onSave: () -> Unit) {
    Text("Buat Todo Baru")
}
