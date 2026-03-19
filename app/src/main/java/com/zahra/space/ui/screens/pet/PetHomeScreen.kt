package com.zahra.space.ui.screens.pet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PetHomeScreen(
    onNavigateToDetail: (Long) -> Unit = {},
    onNavigateToShop: () -> Unit = {}
) {
    val pets = remember {
        listOf(
            Pet(1, "Luna", "Cat", 70, 80, 90, 5),
            Pet(2, "Milo", "Dog", 60, 100, 50, 3),
            Pet(3, "Tweety", "Bird", 90, 70, 80, 2)
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Virtual Pet") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    IconButton(onClick = onNavigateToShop) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Shop")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(pets) { pet ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigateToDetail(pet.id) }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${pet.name} (${pet.type})",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Level ${pet.level}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Text(
                                text = when {
                                    pet.happiness > 80 -> "😊"
                                    pet.happiness > 50 -> "😐"
                                    else -> "😢"
                                },
                                fontSize = 24.sp
                            )
                        }
                        
                        StatBar("Hunger", pet.hunger)
                        StatBar("Happiness", pet.happiness)
                        StatBar("Energy", pet.energy)
                    }
                }
            }
        }
    }
}

@Composable
fun StatBar(label: String, value: Int) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("$label:", style = MaterialTheme.typography.bodySmall)
            Text("$value%", style = MaterialTheme.typography.bodySmall)
        }
        LinearProgressIndicator(
            progress = value / 100f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

data class Pet(
    val id: Long,
    val name: String,
    val type: String,
    val hunger: Int,
    val happiness: Int,
    val energy: Int,
    val level: Int
)

@Composable
fun PetDetailScreen(petId: Long) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Pet Detail") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Pet ID: $petId", style = MaterialTheme.typography.headlineMedium)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {}) { Text("🍖 Kasih Makan") }
                Button(onClick = {}) { Text("🎾 Ajak Main") }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("🧼 Mandikan")
            }
        }
    }
}

@Composable
fun PetShopScreen() {
    val shopItems = remember {
        listOf(
            ShopItem("Makanan Kucing", 50, "🍖"),
            ShopItem("Mainan Bola", 30, "🎾"),
            ShopItem("Vitamin", 100, "💊"),
            ShopItem("Topi Lucu", 200, "🧢"),
            ShopItem("Tempat Tidur", 500, "🛏️"),
            ShopItem("Kalung Emas", 1000, "📿")
        )
    }
    
    Scaffold(
        topBar = { TopAppBar(title = { Text("Pet Shop") }) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(shopItems) { item ->
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(item.icon, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(item.name, style = MaterialTheme.typography.bodyLarge)
                        }
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text("${item.price} ✨")
                        }
                    }
                }
            }
        }
    }
}

data class ShopItem(
    val name: String,
    val price: Int,
    val icon: String
)
