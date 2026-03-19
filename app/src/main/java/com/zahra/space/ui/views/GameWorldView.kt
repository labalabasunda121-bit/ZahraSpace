package com.zahra.space.ui.views

import android.content.Context
import android.view.SurfaceView
import android.view.Choreographer
import com.google.android.filament.*
import com.google.android.filament.android.UiHelper
import java.nio.ByteBuffer
import java.nio.ByteOrder

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
        createSimpleCity()
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
    }

    private fun createSimpleCity() {
        // Ground
        createGround()
        
        // Buildings
        createBuilding(-15f, 2f, -10f, 3f, 5f, 3f, 0.7f, 0.7f, 0.7f) // Gray
        createBuilding(10f, 3f, 5f, 4f, 7f, 4f, 0.6f, 0.4f, 0.2f)   // Brown
        createBuilding(20f, 1.5f, -15f, 5f, 4f, 5f, 0.5f, 0.5f, 0.8f) // Blue
        createBuilding(-5f, 2.5f, 15f, 4f, 6f, 4f, 0.8f, 0.8f, 0.2f)  // Yellow
        createBuilding(5f, 2f, -20f, 3f, 5f, 3f, 0.9f, 0.5f, 0.5f)    // Pink
        
        // Masjid (special building)
        createMosque(0f, 3f, 0f)
    }
    
    private fun createGround() {
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
        
        val floatBuffer = ByteBuffer.allocateDirect(vertices.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertices)
            .rewind()
        vertexBuffer.setBufferAt(engine, 0, floatBuffer)
        
        val indexBuffer = IndexBuffer.Builder()
            .indexCount(6)
            .bufferType(IndexBuffer.Builder.IndexType.USHORT)
            .build(engine)
        
        val shortBuffer = ByteBuffer.allocateDirect(indices.size * 2)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
            .put(indices)
            .rewind()
        indexBuffer.setBuffer(engine, shortBuffer)
        
        val material = Material.Builder()
            .payload(engine, """
                {
                    "material": {
                        "name": "ground",
                        "shadingModel": "lit",
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
    
    private fun createBuilding(x: Float, y: Float, z: Float, 
                                width: Float, height: Float, depth: Float,
                                r: Float, g: Float, b: Float) {
        val hw = width / 2
        val hh = height / 2
        val hd = depth / 2
        
        val vertices = floatArrayOf(
            x - hw, y - hh, z - hd,
            x + hw, y - hh, z - hd,
            x + hw, y + hh, z - hd,
            x - hw, y + hh, z - hd,
            x - hw, y - hh, z + hd,
            x + hw, y - hh, z + hd,
            x + hw, y + hh, z + hd,
            x - hw, y + hh, z + hd
        )
        
        val indices = shortArrayOf(
            0,1,2, 0,2,3,
            4,5,6, 4,6,7,
            0,1,5, 0,5,4,
            2,3,7, 2,7,6,
            0,3,7, 0,7,4,
            1,2,6, 1,6,5
        )
        
        val vertexBuffer = VertexBuffer.Builder()
            .vertexCount(8)
            .bufferCount(1)
            .attribute(VertexBuffer.VertexAttribute.POSITION, 0, VertexBuffer.AttributeType.FLOAT3, 0, 12)
            .build(engine)
        
        val floatBuffer = ByteBuffer.allocateDirect(vertices.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertices)
            .rewind()
        vertexBuffer.setBufferAt(engine, 0, floatBuffer)
        
        val indexBuffer = IndexBuffer.Builder()
            .indexCount(36)
            .bufferType(IndexBuffer.Builder.IndexType.USHORT)
            .build(engine)
        
        val shortBuffer = ByteBuffer.allocateDirect(indices.size * 2)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
            .put(indices)
            .rewind()
        indexBuffer.setBuffer(engine, shortBuffer)
        
        val material = Material.Builder()
            .payload(engine, """
                {
                    "material": {
                        "name": "building",
                        "shadingModel": "lit",
                        "parameters": [
                            {
                                "name": "baseColor",
                                "type": "float3",
                                "default": [$r, $g, $b]
                            }
                        ]
                    }
                }
            """.toByteArray())
            .build(engine)
        
        val renderable = EntityManager.get().create()
        RenderableManager.Builder(1)
            .geometry(0, RenderableManager.PrimitiveType.TRIANGLES, vertexBuffer, indexBuffer, 0, 36)
            .material(0, material.defaultInstance)
            .build(engine, renderable)
        
        scene.addEntity(renderable)
    }
    
    private fun createMosque(x: Float, y: Float, z: Float) {
        // Main building
        createBuilding(x, y, z, 8f, 4f, 8f, 0.9f, 0.9f, 0.9f)
        
        // Dome
        createDome(x, y + 2.5f, z, 3f, 0.9f, 0.8f, 0.7f)
        
        // Minaret
        createBuilding(x - 5f, y + 3f, z - 3f, 1f, 7f, 1f, 0.8f, 0.7f, 0.6f)
        createBuilding(x + 5f, y + 3f, z - 3f, 1f, 7f, 1f, 0.8f, 0.7f, 0.6f)
    }
    
    private fun createDome(x: Float, y: Float, z: Float, radius: Float, r: Float, g: Float, b: Float) {
        // Simplified dome (just a sphere-like shape)
        val r2 = radius
        val vertices = floatArrayOf(
            x - r2, y - r2, z - r2,
            x + r2, y - r2, z - r2,
            x + r2, y + r2, z - r2,
            x - r2, y + r2, z - r2,
            x - r2, y - r2, z + r2,
            x + r2, y - r2, z + r2,
            x + r2, y + r2, z + r2,
            x - r2, y + r2, z + r2
        )
        
        val indices = shortArrayOf(
            0,1,2, 0,2,3, 4,5,6, 4,6,7,
            0,1,5, 0,5,4, 2,3,7, 2,7,6,
            0,3,7, 0,7,4, 1,2,6, 1,6,5
        )
        
        val vertexBuffer = VertexBuffer.Builder()
            .vertexCount(8)
            .bufferCount(1)
            .attribute(VertexBuffer.VertexAttribute.POSITION, 0, VertexBuffer.AttributeType.FLOAT3, 0, 12)
            .build(engine)
        
        val floatBuffer = ByteBuffer.allocateDirect(vertices.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertices)
            .rewind()
        vertexBuffer.setBufferAt(engine, 0, floatBuffer)
        
        val indexBuffer = IndexBuffer.Builder()
            .indexCount(36)
            .bufferType(IndexBuffer.Builder.IndexType.USHORT)
            .build(engine)
        
        val shortBuffer = ByteBuffer.allocateDirect(indices.size * 2)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
            .put(indices)
            .rewind()
        indexBuffer.setBuffer(engine, shortBuffer)
        
        val material = Material.Builder()
            .payload(engine, """
                {
                    "material": {
                        "name": "dome",
                        "shadingModel": "lit",
                        "parameters": [
                            {
                                "name": "baseColor",
                                "type": "float3",
                                "default": [$r, $g, $b]
                            }
                        ]
                    }
                }
            """.toByteArray())
            .build(engine)
        
        val renderable = EntityManager.get().create()
        RenderableManager.Builder(1)
            .geometry(0, RenderableManager.PrimitiveType.TRIANGLES, vertexBuffer, indexBuffer, 0, 36)
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
        engine.destroyRenderer(renderer)
        engine.destroyView(view)
        engine.destroyScene(scene)
        engine.destroyCamera(camera)
        engine.destroy()
    }
}
