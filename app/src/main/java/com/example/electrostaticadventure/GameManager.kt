package com.example.electrostaticadventure

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.electrostaticadventure.gameobjects.Field
import com.example.electrostaticadventure.gameobjects.Journeyer
import com.example.electrostaticadventure.gameobjects.Plaque
import com.example.electrostaticadventure.gameobjects.USCharge
import com.example.electrostaticadventure.gameobjects.Wall
import com.example.electrostaticadventure.mathmodule.Vector2D

class GameManager @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    SurfaceView(context, attributes, defStyleAttr), SurfaceHolder.Callback, Runnable {

    companion object {
        const val maxField = 10f
        const val maxTotalField = 100f;
        const val strength = 10f;
        const val scaleFactor = 500f;
        const val speedValue = 5f;
    }

    private var backgroundPaint = Paint();

    var field = Field(30, 10);
    var walls = ArrayList<Wall>();
    var plaques = ArrayList<Plaque>();
    var finish = Plaque(RectF(100f, 100f, 200f, 200f));

    lateinit var thread: Thread;
    lateinit var canvas: Canvas;

    private var drawing = true;
    private var environmentInitialized = false;

    lateinit var journeyer: Journeyer;

    init {
        backgroundPaint.color = Color.rgb(0, 39, 102);
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh);
        field.generateCellArray(w, h);
        field.add(USCharge(1, Vector2D(w.toFloat() / 2, h.toFloat() / 4), context));
        field.add(USCharge(-1, Vector2D(w.toFloat() / 2, h.toFloat() * 3 / 4), context));

        walls.add(Wall(Vector2D(0f, 0f), Vector2D(w.toFloat(), 40f)));
        walls.add(Wall(Vector2D(w.toFloat() - 40f, 0f), Vector2D(w.toFloat(), h.toFloat())));
        walls.add(Wall(Vector2D(0f, 0f), Vector2D(40f, h.toFloat())));
        walls.add(Wall(Vector2D(0f, h.toFloat() - 40f), Vector2D(w.toFloat(), h.toFloat())));

        environmentInitialized = true;
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        field.add(USCharge(1, Vector2D(event.rawX, event.rawY), context))

        return super.onTouchEvent(event)
    }

    private fun draw() {
        if (holder.surface.isValid) {
            canvas = holder.lockCanvas();

            canvas.drawRect(
                0f, 0f, canvas.width * 1F,
                canvas.height * 1F, backgroundPaint
            );
            field.draw(canvas);
            journeyer.draw(canvas);
            for (w in walls) w.draw(canvas);

            holder.unlockCanvasAndPost(canvas);
        }
    }

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
        journeyer = Journeyer(
            field, Vector2D(500f, 1000f),
            80f, 10f, walls, plaques, finish, context
        );
        var previousFrameTime = System.currentTimeMillis();
        while (drawing) {
            val currentFrameTime = System.currentTimeMillis();
            val dt = (currentFrameTime - previousFrameTime).toFloat() / 1000 * speedValue;
            if (environmentInitialized) {
                journeyer.update(dt);
                draw();
            }

            previousFrameTime = currentFrameTime;
        }
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {}

    override fun surfaceDestroyed(p0: SurfaceHolder) {}

}