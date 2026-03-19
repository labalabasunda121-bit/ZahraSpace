package com.zahra.space.ui.screens.pet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetShopScreen() {
    val items = listOf(
        ShopItem("Makanan", "🍖", 50),
        ShopItem("Mainan", "🎾", 30),
        ShopItem("Vitamin", "💊", 100),
        ShopItem("Topi", "🧢", 200)
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pet Shop") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row {
                            Text(text = item.icon)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = item.name)
                        }
                        Button(
                            onClick = { }
                        ) {
                            Text("${item.price} ✨")
                        }
                    }
                }
            }
        }
    }
}

data class ShopItem(val name: String, val icon: String, val price: Int)
