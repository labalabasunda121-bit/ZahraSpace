package com.zahra.space.ui.views

import android.content.Context
import android.graphics.*
import android.view.SurfaceView

/**
 * Versi 2D dari GameWorldView - TIDAK PAKE FILAMENT
 * Menggambar kota 2D yang terlihat seperti 3D menggunakan Canvas
 */
class GameWorldView(context: Context) : SurfaceView(context) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var rotationAngle = 0f

    init {
        // Set background color (sky)
        setBackgroundColor(Color.rgb(135, 206, 235)) // Sky blue
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        // Draw ground
        val groundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(34, 139, 34) // Forest green
            style = Paint.Style.FILL
        }
        
        val groundPath = Path()
        groundPath.moveTo(0f, height * 0.6f)
        groundPath.lineTo(width, height * 0.5f)
        groundPath.lineTo(width, height)
        groundPath.lineTo(0f, height)
        groundPath.close()
        canvas.drawPath(groundPath, groundPaint)

        // Draw buildings
        drawBuilding(canvas, width * 0.2f, height * 0.45f, 80f, 150f, Color.rgb(169, 169, 169)) // Gray
        drawBuilding(canvas, width * 0.4f, height * 0.4f, 100f, 200f, Color.rgb(139, 69, 19))  // Brown
        drawBuilding(canvas, width * 0.6f, height * 0.35f, 120f, 250f, Color.rgb(70, 130, 180)) // Steel blue
        drawBuilding(canvas, width * 0.8f, height * 0.3f, 90f, 180f, Color.rgb(128, 0, 128)) // Purple
        
        // Draw mosque
        drawMosque(canvas, width * 0.5f, height * 0.4f, 150f)
        
        // Draw character (Zahra)
        drawCharacter(canvas, width * 0.3f, height * 0.55f)
        
        // Draw Luna
        drawLuna(canvas, width * 0.7f, height * 0.6f)
        
        // Draw sun
        drawSun(canvas, width - 100f, 100f)
    }

    private fun drawBuilding(canvas: Canvas, x: Float, y: Float, width: Float, height: Float, color: Int) {
        val buildingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = color
            style = Paint.Style.FILL
        }
        
        // Main building
        canvas.drawRect(x - width/2, y - height, x + width/2, y, buildingPaint)
        
        // Windows
        val windowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.YELLOW
            style = Paint.Style.FILL
        }
        
        for (i in 1..3) {
            for (j in 1..4) {
                canvas.drawRect(
                    x - width/4 + (i-1) * width/3,
                    y - height/5 * j,
                    x - width/4 + (i-1) * width/3 + width/6,
                    y - height/5 * j + height/8,
                    windowPaint
                )
            }
        }
    }
    
    private fun drawMosque(canvas: Canvas, x: Float, y: Float, size: Float) {
        val mosquePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.FILL
        }
        
        // Main building
        canvas.drawRect(x - size/2, y - size*0.7f, x + size/2, y, mosquePaint)
        
        // Dome
        val domePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(255, 215, 0) // Gold
            style = Paint.Style.FILL
        }
        canvas.drawCircle(x, y - size*0.8f, size*0.3f, domePaint)
        
        // Minarets
        canvas.drawRect(x - size*0.7f, y - size*0.5f, x - size*0.5f, y, mosquePaint)
        canvas.drawRect(x + size*0.5f, y - size*0.5f, x + size*0.7f, y, mosquePaint)
    }
    
    private fun drawCharacter(canvas: Canvas, x: Float, y: Float) {
        val charPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        
        // Body (hijab)
        charPaint.color = Color.BLACK
        canvas.drawCircle(x, y - 30f, 20f, charPaint)
        
        // Face
        charPaint.color = Color.rgb(255, 224, 189) // Skin color
        canvas.drawCircle(x, y - 35f, 10f, charPaint)
        
        // Eyes
        charPaint.color = Color.BLACK
        canvas.drawCircle(x - 4f, y - 38f, 2f, charPaint)
        canvas.drawCircle(x + 4f, y - 38f, 2f, charPaint)
    }
    
    private fun drawLuna(canvas: Canvas, x: Float, y: Float) {
        val lunaPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        
        // Body
        lunaPaint.color = Color.WHITE
        canvas.drawCircle(x, y - 20f, 15f, lunaPaint)
        
        // Head
        canvas.drawCircle(x, y - 35f, 10f, lunaPaint)
        
        // Ears
        lunaPaint.color = Color.rgb(255, 200, 200) // Pinkish
        canvas.drawCircle(x - 8f, y - 43f, 5f, lunaPaint)
        canvas.drawCircle(x + 8f, y - 43f, 5f, lunaPaint)
        
        // Eyes
        lunaPaint.color = Color.BLACK
        canvas.drawCircle(x - 4f, y - 37f, 2f, lunaPaint)
        canvas.drawCircle(x + 4f, y - 37f, 2f, lunaPaint)
    }
    
    private fun drawSun(canvas: Canvas, x: Float, y: Float) {
        val sunPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(255, 165, 0) // Orange
            style = Paint.Style.FILL
        }
        canvas.drawCircle(x, y, 40f, sunPaint)
    }
}
