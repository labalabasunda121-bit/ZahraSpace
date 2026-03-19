package com.zahra.space.ui.views

import android.content.Context
import android.view.SurfaceView
import android.view.Choreographer
import com.google.android.filament.*
import com.google.android.filament.android.UiHelper
import com.google.android.filament.gltfio.*
import java.nio.ByteBuffer

class GameWorldView(context: Context) : SurfaceView(context), Choreographer.FrameCallback {
    private val engine = Engine.create()
    private val renderer = engine.createRenderer()
    private val scene = engine.createScene()
    private val view = engine.createView()
    private val camera = engine.createCamera(engine.entityManager.create())
    private val uiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK)
    private val gltfio = Gltfio()
    private val assetLoader = AssetLoader(engine, MaterialProvider(), EntityManager.get())

    init {
        uiHelper.attachTo(this)
        camera.setProjection(45.0, 1.0, 0.1, 1000.0, Camera.Fov.VERTICAL)
        camera.lookAt(0.0, 5.0, 20.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0)
        view.camera = camera
        view.scene = scene
        val light = EntityManager.get().create()
        LightManager.Builder(LightManager.Type.DIRECTIONAL).color(1f,1f,1f).intensity(150000f).direction(0.5f,-1f,0.5f).build(engine,light)
        scene.addEntity(light)
        loadModels()
        Choreographer.getInstance().postFrameCallback(this)
    }
    private fun loadModels() {
        try {
            val cityBytes = context.assets.open("models/city.gltf").readBytes()
            assetLoader.createAssetFromBuffer(ByteBuffer.wrap(cityBytes))?.let { scene.addEntities(it.entities) }
            val zahraBytes = context.assets.open("models/zahra.gltf").readBytes()
            assetLoader.createAssetFromBuffer(ByteBuffer.wrap(zahraBytes))?.let { scene.addEntities(it.entities) }
        } catch (e: Exception) { e.printStackTrace() }
    }
    override fun doFrame(frameTimeNanos: Long) {
        if (uiHelper.isReadyToRender) renderer.render(view)
        Choreographer.getInstance().postFrameCallback(this)
    }
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Choreographer.getInstance().removeFrameCallback(this)
        engine.destroyRenderer(renderer); engine.destroyView(view); engine.destroyScene(scene); engine.destroyCamera(camera); engine.destroy()
    }
}
