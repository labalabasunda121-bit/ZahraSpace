package com.zahra.space.ui.views

import android.content.Context
import android.view.SurfaceView
import android.view.Choreographer
import com.google.android.filament.*
import com.google.android.filament.android.UiHelper
import java.nio.ByteBuffer

class GameWorldView(context: Context) : SurfaceView(context), Choreographer.FrameCallback {
    private val engine = Engine.create()
    private val renderer = engine.createRenderer()
    private val scene = engine.createScene()
    private val view = engine.createView()
    private val camera = engine.createCamera(engine.entityManager.create())
    private val uiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK)

    init {
        uiHelper.attachTo(this)
        setupScene()
        // Tidak perlu load model dulu, cukup scene kosong
        Choreographer.getInstance().postFrameCallback(this)
    }

    private fun setupScene() {
        camera.setProjection(45.0, 1.0, 0.1, 1000.0, Camera.Fov.VERTICAL)
        camera.lookAt(0.0, 5.0, 20.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0)
        view.camera = camera
        view.scene = scene

        // Ambient light
        val light = EntityManager.get().create()
        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(1f, 1f, 1f)
            .intensity(150000f)
            .direction(0.5f, -1f, 0.5f)
            .build(engine, light)
        scene.addEntity(light)
        
        // Tambah ground plane sederhana (biar gak kosong)
        createGroundPlane()
    }
    
    private fun createGroundPlane() {
        // Simple ground plane without textures
        val vertices = floatArrayOf(
            -50f, 0f, -50f,
             50f, 0f, -50f,
             50f, 0f,  50f,
            -50f, 0f,  50f
        )
        
        val indices = shortArrayOf(0, 1, 2, 0, 2, 3)
        
        val vertexBuffer = VertexBuffer.Builder()
            .vertexCount(4)
            .bufferCount(1)
            .attribute(VertexBuffer.VertexAttribute.POSITION, 0, VertexBuffer.AttributeType.FLOAT3, 0, 12)
            .build(engine)
        
        val floatBuffer = java.nio.ByteBuffer.allocateDirect(vertices.size * 4)
            .order(java.nio.ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertices)
            .rewind()
        vertexBuffer.setBufferAt(engine, 0, floatBuffer)
        
        val indexBuffer = IndexBuffer.Builder()
            .indexCount(6)
            .bufferType(IndexBuffer.Builder.IndexType.USHORT)
            .build(engine)
        
        val shortBuffer = java.nio.ByteBuffer.allocateDirect(indices.size * 2)
            .order(java.nio.ByteOrder.nativeOrder())
            .asShortBuffer()
            .put(indices)
            .rewind()
        indexBuffer.setBuffer(engine, shortBuffer)
        
        // Simple material
        val material = Material.Builder()
            .payload(engine, """
                {
                    "material": {
                        "name": "ground",
                        "shadingModel": "unlit",
                        "parameters": [
                            {
                                "name": "baseColor",
                                "type": "float3",
                                "default": [0.2, 0.5, 0.2]
                            }
                        ]
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
        // Destroy in correct order
        engine.destroyRenderer(renderer)
        engine.destroyView(view)
        engine.destroyScene(scene)
        engine.destroyCamera(camera)
        engine.destroy()
    }
}
