package com.zahra.space.ui.views

import android.content.Context
import android.view.SurfaceView
import android.view.Choreographer
import com.google.android.filament.*
import com.google.android.filament.android.UiHelper
import com.google.android.filament.gltfio.*
import java.nio.ByteBuffer

class Luna3DView(context: Context) : SurfaceView(context), Choreographer.FrameCallback {
    private val engine = Engine.create()
    private val renderer = engine.createRenderer()
    private val scene = engine.createScene()
    private val view = engine.createView()
    private val camera = engine.createCamera(engine.entityManager.create())
    private val uiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK)
    
    // CARA BENAR: Pake FilamentAssetLoader
    private val gltfio = Gltfio()
    private val assetLoader = gltfio.createAssetLoader(engine)

    init {
        uiHelper.attachTo(this)
        setupScene()
        loadModel()
        Choreographer.getInstance().postFrameCallback(this)
    }

    private fun setupScene() {
        camera.setProjection(45.0, 1.0, 0.1, 100.0, Camera.Fov.VERTICAL)
        camera.lookAt(0.0, 1.0, 3.0, 0.0, 0.5, 0.0, 0.0, 1.0, 0.0)
        view.camera = camera
        view.scene = scene

        // Ambient light
        val light = EntityManager.get().create()
        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(1f, 1f, 1f)
            .intensity(100000f)
            .direction(0f, -1f, 0f)
            .build(engine, light)
        scene.addEntity(light)
    }

    private fun loadModel() {
        try {
            val bytes = context.assets.open("models/luna.gltf").readBytes()
            val buffer = ByteBuffer.wrap(bytes)
            val asset = assetLoader.createAsset(buffer)
            asset?.let { scene.addEntities(it.entities) }
        } catch (e: Exception) {
            // Skip if model not found
        }
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
        // CARA BENAR: destroy semuanya
        engine.destroyRenderer(renderer)
        engine.destroyView(view)
        engine.destroyScene(scene)
        engine.destroyCamera(camera)
        engine.destroy()
    }
}
