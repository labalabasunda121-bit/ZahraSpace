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
        createLunaShape()
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

    private fun createLunaShape() {
        // Body
        createSphere(0f, 0f, 0f, 0.6f, floatArrayOf(1f, 0.9f, 0.9f))
        // Head
        createSphere(0f, 0.8f, 0f, 0.4f, floatArrayOf(1f, 0.9f, 0.9f))
        // Ears
        createCone(-0.3f, 1.1f, 0f, 0.2f, 0.3f, floatArrayOf(1f, 0.8f, 0.8f))
        createCone(0.3f, 1.1f, 0f, 0.2f, 0.3f, floatArrayOf(1f, 0.8f, 0.8f))
        // Tail
        createCone(-0.7f, -0.3f, -0.3f, 0.15f, 0.6f, floatArrayOf(1f, 0.8f, 0.8f))
    }

    private fun createSphere(x: Float, y: Float, z: Float, radius: Float, color: FloatArray) {
        // Simplified: use a cube as sphere placeholder
        val r = radius
        val verts = floatArrayOf(
            x - r, y - r, z - r,
            x + r, y - r, z - r,
            x + r, y + r, z - r,
            x - r, y + r, z - r,
            x - r, y - r, z + r,
            x + r, y - r, z + r,
            x + r, y + r, z + r,
            x - r, y + r, z + r
        )
        
        val indices = shortArrayOf(
            0,1,2, 0,2,3, 4,5,6, 4,6,7,
            0,1,5, 0,5,4, 2,3,7, 2,7,6,
            0,3,7, 0,7,4, 1,2,6, 1,6,5
        )
        
        createMesh(verts, indices, color)
    }

    private fun createCone(x: Float, y: Float, z: Float, radius: Float, height: Float, color: FloatArray) {
        // Simplified: use a pyramid-like shape
        val hh = height / 2
        val verts = floatArrayOf(
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
        
        createMesh(verts, indices, color)
    }

    private fun createMesh(vertices: FloatArray, indices: ShortArray, color: FloatArray) {
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
                        "name": "simple",
                        "shadingModel": "lit",
                        "parameters": [
                            {
                                "name": "baseColor",
                                "type": "float3",
                                "default": [${color[0]}, ${color[1]}, ${color[2]}]
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
