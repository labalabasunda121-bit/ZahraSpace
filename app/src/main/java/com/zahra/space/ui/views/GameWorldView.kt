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
        createSimpleEnvironment()
        Choreographer.getInstance().postFrameCallback(this)
    }

    private fun setupScene() {
        camera.setProjection(45.0, 1.0, 0.1, 1000.0, Camera.Fov.VERTICAL)
        camera.lookAt(0.0, 5.0, 20.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0)
        view.camera = camera
        view.scene = scene

        val light = EntityManager.get().create()
        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(1f, 1f, 1f)
            .intensity(150000f)
            .direction(0.5f, -1f, 0.5f)
            .build(engine, light)
        scene.addEntity(light)
    }

    private fun createSimpleEnvironment() {
        // Create a ground plane
        val groundVerts = floatArrayOf(
            -50f, 0f, -50f,
             50f, 0f, -50f,
             50f, 0f,  50f,
            -50f, 0f,  50f
        )
        val groundIndices = shortArrayOf(0, 1, 2, 0, 2, 3)

        createMesh(groundVerts, groundIndices, floatArrayOf(0.2f, 0.5f, 0.2f))

        // Create some simple buildings
        createBuilding(-15f, 2f, -10f, 3f, 5f, 3f, floatArrayOf(0.5f, 0.5f, 0.5f))
        createBuilding(10f, 3f, 5f, 4f, 7f, 4f, floatArrayOf(0.6f, 0.4f, 0.2f))
        createBuilding(20f, 1.5f, -15f, 5f, 4f, 5f, floatArrayOf(0.7f, 0.7f, 0.7f))
    }

    private fun createBuilding(x: Float, y: Float, z: Float, width: Float, height: Float, depth: Float, color: FloatArray) {
        val hw = width / 2
        val hh = height / 2
        val hd = depth / 2
        
        val verts = floatArrayOf(
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
            0,1,2, 0,2,3, 4,5,6, 4,6,7,
            0,1,5, 0,5,4, 2,3,7, 2,7,6,
            0,3,7, 0,7,4, 1,2,6, 1,6,5
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
