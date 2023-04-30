package com.example.electrostaticadventure

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.electrostaticadventure.gameobjects.Field
import com.example.electrostaticadventure.gameobjects.USCharge
import com.example.electrostaticadventure.mathmodule.Vector2D

class GameManager @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0):
    SurfaceView(context, attributes,defStyleAttr), SurfaceHolder.Callback, Runnable {

    companion object {
        const val maxField = 10f
        const val maxTotalField = 100f;
        const val strength = 10f;
        const val scaleFactor = 500f;
    }

    private var backgroundPaint = Paint();

    var field: Field;
    lateinit var thread: Thread;
    lateinit var canvas: Canvas;
    var drawing = true;

    init {
        backgroundPaint.color = Color.rgb(0, 39, 102);
        field = Field(30, 10);
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh);
        field.generateCellArray(w, h);
        field.add(USCharge(1, Vector2D(w.toFloat()/2, h.toFloat()/4), context));
        field.add(USCharge(-1, Vector2D(w.toFloat()/2, h.toFloat() * 3/4), context));


    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        field.add(USCharge(1, Vector2D(event.rawX, event.rawY), context))

        return super.onTouchEvent(event)
    }

    private fun draw() {
        if (holder.surface.isValid) {
            canvas = holder.lockCanvas();

            canvas.drawRect(0f, 0f, canvas.width *1F,
                canvas.height *1F, backgroundPaint);
            field.draw(canvas);

            holder.unlockCanvasAndPost(canvas);
        }
    }



    override fun surfaceCreated(p0: SurfaceHolder) {
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {}

    override fun surfaceDestroyed(p0: SurfaceHolder) {}

    public fun pause() {
        drawing = false;
        thread.join();
    }

    public fun resume() {
        drawing = true;
        thread = Thread(this);
        thread.start();
    }

    override fun run() {
        while(drawing) {
            draw();
        }
    }
}