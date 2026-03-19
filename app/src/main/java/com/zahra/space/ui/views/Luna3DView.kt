package com.zahra.space.ui.views

import android.content.Context
import android.graphics.*
import android.view.SurfaceView

/**
 * Versi 2D dari Luna3DView - TIDAK PAKE FILAMENT
 * Menggambar kucing Luna menggunakan Canvas
 */
class Luna3DView(context: Context) : SurfaceView(context) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cx = width / 2f
        val cy = height / 2f

        // Body
        paint.color = Color.WHITE
        canvas.drawCircle(cx, cy + 20f, 40f, paint)

        // Head
        canvas.drawCircle(cx, cy - 20f, 30f, paint)

        // Ears
        paint.color = Color.rgb(255, 200, 200) // Pinkish
        canvas.drawCircle(cx - 20f, cy - 45f, 15f, paint)
        canvas.drawCircle(cx + 20f, cy - 45f, 15f, paint)

        // Eyes
        paint.color = Color.BLACK
        canvas.drawCircle(cx - 12f, cy - 30f, 5f, paint)
        canvas.drawCircle(cx + 12f, cy - 30f, 5f, paint)

        // Nose
        paint.color = Color.rgb(255, 150, 150) // Pink
        canvas.drawCircle(cx, cy - 20f, 4f, paint)

        // Whiskers
        paint.color = Color.BLACK
        paint.strokeWidth = 2f
        canvas.drawLine(cx - 15f, cy - 20f, cx - 30f, cy - 25f, paint)
        canvas.drawLine(cx - 15f, cy - 20f, cx - 30f, cy - 15f, paint)
        canvas.drawLine(cx + 15f, cy - 20f, cx + 30f, cy - 25f, paint)
        canvas.drawLine(cx + 15f, cy - 20f, cx + 30f, cy - 15f, paint)

        // Tail
        paint.color = Color.WHITE
        paint.strokeWidth = 10f
        val tailPath = Path()
        tailPath.moveTo(cx + 35f, cy + 25f)
        tailPath.quadTo(cx + 60f, cy + 10f, cx + 50f, cy - 10f)
        canvas.drawPath(tailPath, paint)
    }
}
