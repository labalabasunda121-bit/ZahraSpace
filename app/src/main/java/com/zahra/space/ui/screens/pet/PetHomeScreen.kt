package com.zahra.space.ui.screens.pet
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zahra.space.ui.views.Luna3DView
import com.zahra.space.viewmodel.PetViewModel

@Composable
fun PetHomeScreen(onNavigateToShop: () -> Unit) {
    val vm: PetViewModel = viewModel()
    val pet by vm.petState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Luna") },
                actions = { IconButton(onNavigateToShop) { Icon(Icons.Default.ShoppingCart, null) } }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            AndroidView(factory = { ctx -> Luna3DView(ctx) }, Modifier.size(200.dp))
            Spacer(Modifier.height(16.dp))
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    StatBar("Lapar", pet.hunger)
                    StatBar("Senang", pet.happiness)
                    StatBar("Bersih", pet.cleanliness)
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                ActionButton("🍖") { vm.feed() }
                ActionButton("🎾") { vm.play() }
                ActionButton("🧼") { vm.clean() }
            }
        }
    }
}
@Composable fun StatBar(label: String, value: Int) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, Modifier.width(60.dp))
        LinearProgressIndicator(value / 100f, Modifier.weight(1f))
        Text("$value%", Modifier.width(40.dp))
    }
}
@Composable fun ActionButton(icon: String, onClick: () -> Unit) {
    Button(onClick, Modifier.size(60.dp)) { Text(icon, fontSize = 20.sp) }
}
