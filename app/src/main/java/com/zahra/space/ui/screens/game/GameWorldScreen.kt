package com.zahra.space.ui.screens.game
@file:Suppress("ExperimentalMaterial3Api")

import android.graphics.Rect
import android.opengl.Matrix
import android.content.Context
import android.view.Choreographer
import android.view.SurfaceView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.filament.*
import com.google.android.filament.android.UiHelper
import kotlinx.coroutines.delay
import java.nio.ByteBuffer
import java.nio.ByteOrder

@Composable
fun GameWorldScreen() {
    var positionX by remember { mutableFloatStateOf(0f) }
    var positionY by remember { mutableFloatStateOf(0f) }
    var positionZ by remember { mutableFloatStateOf(0f) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var interactionCount by remember { mutableIntStateOf(0) }
    var gameTime by remember { mutableLongStateOf(0L) }
    var weather by remember { mutableStateOf("Cerah") }
    var balance by remember { mutableIntStateOf(0) }
    var currentPrayer by remember { mutableStateOf("") }
    var showPrayerNotif by remember { mutableStateOf(false) }

    val hiddenMessages = listOf(
        "Ada yang titip pesan: 'Jangan lupa bahagia, walau jauh.'",
        "Jagalah hati yang kau jaga, karena dia menjaga hatimu juga.",
        "Semoga harimu indah.",
        "Kalau ketemu Zahra, bilang aku bangga.",
        "Aku di sini, menjaga hatimu.",
        "Setiap senja, aku mendoakanmu.",
        "Jangan pernah merasa sendiri.",
        "Z"
    )

    val npcMessages = listOf(
        "Assalamu'alaikum, Zahra!",
        "Jangan lupa sholat ya...",
        "Hari ini cerah sekali.",
        "Kucingmu lucu sekali.",
        "Doaku selalu untukmu."
    )

    // Game time progression
    LaunchedEffect(Unit) {
        while (true) {
            delay(60000)
            gameTime += 3600000
            
            val gameHour = (gameTime / 3600000) % 24
            if (gameHour.toInt() % 6 == 0 && !showPrayerNotif) {
                currentPrayer = when (gameHour.toInt()) {
                    6 -> "Subuh"
                    12 -> "Dzuhur"
                    18 -> "Ashar"
                    0 -> "Maghrib"
                    5 -> "Isya"
                    else -> ""
                }
                if (currentPrayer.isNotEmpty()) {
                    showPrayerNotif = true
                }
            }
        }
    }

    // Weather system
    LaunchedEffect(Unit) {
        val weathers = listOf("Cerah", "Berawan", "Hujan Ringan", "Hujan Deras", "Mendung")
        while (true) {
            delay(300000)
            weather = weathers.random()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context -> FilamentView(context) },
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("🏙️ Kota Zahra")
                        Text("💰 $balance")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("⏰ ${gameTime / 3600000}:00")
                        Text("🌤️ $weather")
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (showPrayerNotif) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("🕌 WAKTU SHOLAT $currentPrayer")
                        Text("Ayo ke masjid!")
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(onClick = {
                                positionX = 10f
                                positionZ = 10f
                                showPrayerNotif = false
                                balance += 50
                            }) {
                                Text("Teleport (+50✨)")
                            }
                            Button(onClick = { showPrayerNotif = false }) {
                                Text("Nanti")
                            }
                        }
                    }
                }
            }

            if (interactionCount % 5 == 0 && interactionCount > 0) {
                Card(
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.End)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("F", fontSize = 20.sp)
                    }
                }
            }

            if (showDialog) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(dialogMessage)
                        Button(
                            onClick = { showDialog = false },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Tutup")
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { positionX -= 1f }) { Text("←") }
                Button(onClick = { positionZ += 0.5f }) { Text("↑") }
                Button(onClick = { positionX += 1f }) { Text("→") }
                Button(onClick = { positionZ -= 0.5f }) { Text("↓") }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    interactionCount++
                    if (Math.random() < 0.2) {
                        dialogMessage = hiddenMessages.random()
                        balance += 10
                    } else {
                        dialogMessage = npcMessages.random()
                    }
                    showDialog = true
                }) {
                    Text("💬 Ngobrol")
                }
                Button(onClick = {
                    positionX = 10f
                    positionZ = 10f
                    dialogMessage = "Masjid Nurul Huda"
                    showDialog = true
                }) {
                    Text("🕌 Masjid")
                }
                Button(onClick = {
                    positionX = -10f
                    positionZ = 5f
                    dialogMessage = "Zahra Bistro"
                    showDialog = true
                }) {
                    Text("🍳 Resto")
                }
                Button(onClick = {
                    positionX = 5f
                    positionZ = -10f
                    dialogMessage = "Minimart"
                    showDialog = true
                }) {
                    Text("🛒 Belanja")
                }
            }
        }
    }
}

class FilamentView(context: Context) : SurfaceView(context), Choreographer.FrameCallback {
    private val engine = Engine.create()
    private val renderer = engine.createRenderer()
    private val scene = engine.createScene()
    private val view = engine.createView()
    private val camera = engine.createCamera(engine.entityManager.create())
    private val uiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK)
    private val models = mutableListOf<Entity>()

    init {
        uiHelper.attachTo(this)
        setupScene()
        Choreographer.getInstance().postFrameCallback(this)
    }

    private fun setupScene() {
        camera.setProjection(45.0, 1.0, 0.1, 100.0, Camera.Fov.VERTICAL)
        camera.lookAt(0.0, 2.0, 5.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0)
        view.camera = camera
        view.scene = scene

        val ambientLight = EntityManager.get().create()
        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(1.0f, 1.0f, 1.0f)
            .intensity(100000.0f)
            .direction(0.0f, -1.0f, 0.0f)
            .build(engine, ambientLight)
        scene.addEntity(ambientLight)
        
        createGroundPlane()
    }

    private fun createGroundPlane() {
        val vertexData = floatArrayOf(
            -20f, 0f, -20f,  0f, 0.5f, 0f,
             20f, 0f, -20f,  0f, 0.5f, 0f,
            -20f, 0f,  20f,  0f, 0.5f, 0f,
             20f, 0f,  20f,  0f, 0.5f, 0f
        )

        val indexData = shortArrayOf(0, 1, 2, 1, 3, 2)

        val vertexBuffer = VertexBuffer.Builder()
            .vertexCount(4)
            .bufferCount(1)
            .attribute(VertexBuffer.VertexAttribute.POSITION, 0, VertexBuffer.AttributeType.FLOAT3, 0, 24)
            .attribute(VertexBuffer.VertexAttribute.COLOR, 0, VertexBuffer.AttributeType.FLOAT3, 12, 24)
            .build(engine)
        
        val floatBuffer = ByteBuffer.allocateDirect(vertexData.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexData)
            .rewind()
        vertexBuffer.setBufferAt(engine, 0, floatBuffer)

        val indexBuffer = IndexBuffer.Builder()
            .indexCount(6)
            .bufferType(IndexBuffer.Builder.IndexType.USHORT)
            .build(engine)
        
        val shortBuffer = ByteBuffer.allocateDirect(indexData.size * 2)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
            .put(indexData)
            .rewind()
        indexBuffer.setBuffer(engine, shortBuffer)

        val material = Material.Builder()
            .payload(engine, """
                {
                    "material": {
                        "name": "ground",
                        "shadingModel": "unlit"
                    }
                }
            """.toByteArray())
            .build(engine)

        val renderable = EntityManager.get().create()
        RenderableManager.Builder(1)
            .geometry(0, RenderableManager.PrimitiveType.TRIANGLES, vertexBuffer, indexBuffer, 0, 6)
            .material(0, material.defaultInstance)
            .build(engine, renderable)

        scene.addEntity(renderable)
        models.add(renderable)
    }

    override fun doFrame(frameTimeNanos: Long) {
        if (uiHelper.isReadyToRender) {
            renderer.render(view)
        }
        Choreographer.getInstance().postFrameCallback(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Choreographer.getInstance().removeFrameCallback(this)
        models.forEach { engine.destroyEntity(it) }
        engine.destroyRenderer(renderer)
        engine.destroyView(view)
        engine.destroyScene(scene)
        camera.destroy(engine)
        engine.destroy()
    }
}
