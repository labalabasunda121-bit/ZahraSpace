package com.zahra.space.ui.screens.todo

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailScreen(todoId: Long) {
    Text("Detail Todo #$todoId")
}
