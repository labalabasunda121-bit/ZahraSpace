package com.zahra.space.ui.screens.pet

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zahra.space.ui.views.Luna3DView
import com.zahra.space.viewmodel.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetHomeScreen(onNavigateToShop: () -> Unit) {
    val vm: PetViewModel = viewModel()
    val pet by vm.petState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Luna") },
                actions = {
                    IconButton(onClick = onNavigateToShop) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Shop")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AndroidView(
                factory = { ctx -> Luna3DView(ctx) },
                modifier = Modifier.size(200.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    StatBar("Lapar", pet.hunger)
                    StatBar("Senang", pet.happiness)
                    StatBar("Bersih", pet.cleanliness)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionButton("🍖") { vm.feed() }
                ActionButton("🎾") { vm.play() }
                ActionButton("🧼") { vm.clean() }
            }
        }
    }
}

@Composable
fun StatBar(label: String, value: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, modifier = Modifier.width(60.dp))
        LinearProgressIndicator(
            progress = value / 100f,
            modifier = Modifier.weight(1f)
        )
        Text(text = "$value%", modifier = Modifier.width(40.dp))
    }
}

@Composable
fun ActionButton(icon: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(60.dp)
    ) {
        Text(icon, fontSize = 20.sp)
    }
}
