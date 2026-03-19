package com.zahra.space.ui.views

import android.content.Context
import android.view.SurfaceView
import android.view.Choreographer
import com.google.android.filament.*
import com.google.android.filament.android.UiHelper
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Luna3DView(context: Context) : SurfaceView(context), Choreographer.FrameCallback {
    private val engine = Engine.create()
    private val renderer = engine.createRenderer()
    private val scene = engine.createScene()
    private val view = engine.createView()
    private val camera = engine.createCamera(engine.entityManager.create())
    private val uiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK)

    init {
        uiHelper.attachTo(this)
        setupScene()
        createLuna()
        Choreographer.getInstance().postFrameCallback(this)
    }

    private fun setupScene() {
        camera.setProjection(45.0, 1.0, 0.1, 100.0, Camera.Fov.VERTICAL)
        camera.lookAt(0.0, 1.0, 3.0, 0.0, 0.5, 0.0, 0.0, 1.0, 0.0)
        view.camera = camera
        view.scene = scene

        val light = EntityManager.get().create()
        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(1f, 1f, 1f)
            .intensity(100000f)
            .direction(0f, -1f, 0f)
            .build(engine, light)
        scene.addEntity(light)
    }

    private fun createLuna() {
        // Body
        createEllipsoid(0f, 0f, 0f, 0.6f, 0.4f, 0.6f, 1f, 0.9f, 0.9f)
        
        // Head
        createSphere(0f, 0.7f, 0f, 0.3f, 1f, 0.9f, 0.9f)
        
        // Ears
        createCone(-0.2f, 1.0f, 0f, 0.15f, 0.25f, 1f, 0.8f, 0.8f)
        createCone(0.2f, 1.0f, 0f, 0.15f, 0.25f, 1f, 0.8f, 0.8f)
        
        // Tail
        createCone(-0.6f, -0.2f, -0.3f, 0.1f, 0.4f, 1f, 0.8f, 0.8f)
        
        // Eyes
        createSphere(-0.1f, 0.8f, 0.2f, 0.05f, 0f, 0f, 0f)
        createSphere(0.1f, 0.8f, 0.2f, 0.05f, 0f, 0f, 0f)
    }
    
    private fun createSphere(x: Float, y: Float, z: Float, radius: Float, r: Float, g: Float, b: Float) {
        val vertices = floatArrayOf(
            x - radius, y - radius, z - radius,
            x + radius, y - radius, z - radius,
            x + radius, y + radius, z - radius,
            x - radius, y + radius, z - radius,
            x - radius, y - radius, z + radius,
            x + radius, y - radius, z + radius,
            x + radius, y + radius, z + radius,
            x - radius, y + radius, z + radius
        )
        
        val indices = shortArrayOf(
            0,1,2, 0,2,3, 4,5,6, 4,6,7,
            0,1,5, 0,5,4, 2,3,7, 2,7,6,
            0,3,7, 0,7,4, 1,2,6, 1,6,5
        )
        
        createMesh(vertices, indices, r, g, b)
    }
    
    private fun createEllipsoid(x: Float, y: Float, z: Float, 
                                 rx: Float, ry: Float, rz: Float,
                                 r: Float, g: Float, b: Float) {
        val vertices = floatArrayOf(
            x - rx, y - ry, z - rz,
            x + rx, y - ry, z - rz,
            x + rx, y + ry, z - rz,
            x - rx, y + ry, z - rz,
            x - rx, y - ry, z + rz,
            x + rx, y - ry, z + rz,
            x + rx, y + ry, z + rz,
            x - rx, y + ry, z + rz
        )
        
        val indices = shortArrayOf(
            0,1,2, 0,2,3, 4,5,6, 4,6,7,
            0,1,5, 0,5,4, 2,3,7, 2,7,6,
            0,3,7, 0,7,4, 1,2,6, 1,6,5
        )
        
        createMesh(vertices, indices, r, g, b)
    }
    
    private fun createCone(x: Float, y: Float, z: Float, radius: Float, height: Float, r: Float, g: Float, b: Float) {
        val hh = height / 2
        val vertices = floatArrayOf(
            x - radius, y - hh, z - radius,
            x + radius, y - hh, z - radius,
            x + radius, y - hh, z + radius,
            x - radius, y - hh, z + radius,
            x, y + hh, z
        )
        
        val indices = shortArrayOf(
            0,1,4, 1,2,4, 2,3,4, 3,0,4,
            0,1,2, 0,2,3
        )
        
        createMesh(vertices, indices, r, g, b)
    }
    
    private fun createMesh(vertices: FloatArray, indices: ShortArray, r: Float, g: Float, b: Float) {
        val vertexBuffer = VertexBuffer.Builder()
            .vertexCount(vertices.size / 3)
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
            .indexCount(indices.size)
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
                        "name": "luna",
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
            .geometry(0, RenderableManager.PrimitiveType.TRIANGLES, vertexBuffer, indexBuffer, 0, indices.size)
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
