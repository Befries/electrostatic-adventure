package com.example.electrostaticadventure

import android.annotation.SuppressLint
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
import com.example.electrostaticadventure.uiobjects.LaunchButton

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
        const val phoneRatio = 16f/9;
    }

    private var backgroundPaint = Paint();
    var gameState = GameStatus.MENU;

    private var menuDrawers = ArrayList<Drawer>();
    private var gameDrawers = ArrayList<Drawer>();

    lateinit var launchButton: LaunchButton;

    private var field = Field(30, 10);
    private var walls = ArrayList<Wall>();
    private var plaques = ArrayList<Plaque>();
    private var finish = Plaque(RectF(100f, 100f, 200f, 200f));

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

        generateMenu(w.toFloat(), h.toFloat());
        generateLayout(w.toFloat(), h.toFloat());
        environmentInitialized = true;
    }


    private fun generateMenu(w: Float, h: Float) {
        // length = two times height -> length = 6w/8 -> height = 3w/8
        launchButton = LaunchButton(RectF(w/8, h/8, 7*w/8, (h + 3*w)/8),
            context, R.drawable.launch_button_down, R.drawable.launch_button_up,
            R.drawable.launch_button_down, this);

        menuDrawers.add(launchButton);
    }

    private fun generateLayout(w: Float, h: Float) {
        field.generateCellArray(w.toInt(), h.toInt());
        field.add(USCharge(1, Vector2D(w / 2, h / 4), context));
        field.add(USCharge(-1, Vector2D(w / 2, h * 3 / 4), context));

        walls.add(Wall(Vector2D(0f, 0f), Vector2D(w, 40f)));
        walls.add(Wall(Vector2D(w - 40f, 0f), Vector2D(w, h)));
        walls.add(Wall(Vector2D(0f, 0f), Vector2D(40f, h)));
        walls.add(Wall(Vector2D(0f, h - 40f), Vector2D(w, h)));
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        // buttons activates during release
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                when(gameState) {
                    GameStatus.MENU -> {
                        launchButton.press(event.rawX, event.rawY);
                    }
                    GameStatus.DEPLOYMENT -> {

                    }
                    GameStatus.RUNNING -> {

                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                when(gameState) {
                    GameStatus.MENU -> {
                        launchButton.checkAndActivate(event.rawX, event.rawY);
                    }
                    GameStatus.DEPLOYMENT -> {
                        field.add(USCharge(1, Vector2D(event.rawX, event.rawY), context));
                    }
                    GameStatus.RUNNING -> {}
                }
            }
        }

        // return true else the app won't take count of the action_up.
        return true;
    }

    private fun draw() {
        if (holder.surface.isValid) {
            canvas = holder.lockCanvas();

            canvas.drawRect(
                0f, 0f, canvas.width * 1F,
                canvas.height * 1F, backgroundPaint
            );

            when(gameState) {
                GameStatus.MENU -> for (drawn in menuDrawers) drawn.draw(canvas);
                else -> drawGameLayout(canvas);
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }


    private fun drawGameLayout(canvas: Canvas?) {
        for (w in walls) w.draw(canvas);
        field.draw(canvas);
        journeyer.draw(canvas);
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
            80f, 10f, walls, plaques, finish, context);
        var previousFrameTime = System.currentTimeMillis();

        while (drawing) {
            // calculate time passed between frame
            val currentFrameTime = System.currentTimeMillis();
            val delayTime = (currentFrameTime - previousFrameTime).toFloat() / 1000 * speedValue;

            if (!environmentInitialized) continue;
            when (gameState) {
                GameStatus.MENU -> {}
                GameStatus.DEPLOYMENT -> {
                    // temporary for tests
                    journeyer.update(delayTime);
                }
                GameStatus.RUNNING -> {
                    journeyer.update(delayTime);
                }
            }
            draw();
            previousFrameTime = currentFrameTime;
        }
    }



    override fun surfaceCreated(p0: SurfaceHolder) {
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {}

    override fun surfaceDestroyed(p0: SurfaceHolder) {}

}