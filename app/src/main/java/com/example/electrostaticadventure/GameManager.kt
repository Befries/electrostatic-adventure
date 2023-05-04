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
import com.example.electrostaticadventure.gameobjects.plaques.Plaque
import com.example.electrostaticadventure.gameobjects.USCharge
import com.example.electrostaticadventure.gameobjects.Wall
import com.example.electrostaticadventure.gameobjects.plaques.FinishPlaque
import com.example.electrostaticadventure.mathmodule.Vector2D
import com.example.electrostaticadventure.uiobjects.GameButton
import com.example.electrostaticadventure.uiobjects.LaunchButton
import com.example.electrostaticadventure.uiobjects.MenuButton

class GameManager @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    SurfaceView(context, attributes, defStyleAttr), SurfaceHolder.Callback, Runnable {

    companion object {
        const val maxField = 10f;
        const val maxTotalField = 100f;
        const val strength = 50f;
        const val scaleFactor = 500f;
        const val speedValue = 5f;
    }

    private var backgroundPaint = Paint();
    lateinit var playgroundArea: RectF;
    var gameState = GameStatus.MENU;

    private var menuDrawers = ArrayList<Drawer>();
    private var gameDrawers = ArrayList<Drawer>();

    private var gameButtons = ArrayList<GameButton>();

    lateinit var launchButton: LaunchButton;
    lateinit var backToMenuButton: MenuButton;

    private var field = Field(30, 10);
    private var finishPlaque = FinishPlaque(RectF(0f, 0f, 100f, 100f),
        this);
    private var walls = ArrayList<Wall>();
    private var plaques = ArrayList<Plaque>();

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
        /*
         define where the game is played understanding that the top and bottom
         are occupied by buttons
         */
        val playgroundOrigin = Vector2D(100f, 100f);
        val playgroundWidth = 2;
        val playgroundHeight = 3;
        generateGameLayout(w.toFloat(), h.toFloat(), playgroundOrigin);
        generateGameUI(w.toFloat(), h.toFloat());

        playgroundArea = RectF(
            playgroundOrigin.x, playgroundOrigin.y,
            playgroundOrigin.x + w.toFloat(), playgroundOrigin.y + h.toFloat()
        );
        environmentInitialized = true;
    }


    private fun generateMenu(w: Float, h: Float) {
        // length = two times height -> length = 6w/8 -> height = 3w/8
        launchButton = LaunchButton(
            RectF(w / 8, h / 8, 7 * w / 8, (h + 3 * w) / 8),
            context, R.drawable.launch_button_down, R.drawable.launch_button_up,
            R.drawable.launch_button_down, this
        );
        menuDrawers.add(launchButton);
        gameButtons.add(launchButton);
    }

    private fun generateGameUI(w: Float, h: Float) {
        // length * 40 = height * 64 -> length = w/4 -> height = 5*w/32
        backToMenuButton = MenuButton(
            RectF(0f, 0f, w / 4, 5 * w / 32),
            context,
            R.drawable.menu_button_down,
            R.drawable.menu_button_up,
            R.drawable.menu_button_down,
            this
        );
        gameDrawers.add(backToMenuButton);
        gameButtons.add(backToMenuButton);
    }

    private fun generateGameLayout(w: Float, h: Float, origin: Vector2D) {
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
            MotionEvent.ACTION_DOWN -> onTouchDown(event.rawX, event.rawY - 65f);
            MotionEvent.ACTION_UP -> onTouchUP(event.rawX, event.rawY - 65f);
        }

        // return true else the app won't take count of the action_up.
        return true;
    }

    private fun onTouchDown(x: Float, y: Float) {
        when (gameState) {
            GameStatus.MENU -> {
                launchButton.press(x, y);
            }

            else -> {
                if (playgroundArea.contains(x, y)) field.add(USCharge(1, Vector2D(x, y), context));
                backToMenuButton.press(x, y);
            }
        }
    }

    private fun onTouchUP(x: Float, y: Float) {
        when (gameState) {
            GameStatus.MENU -> {
                launchButton.checkAndActivate(x, y);
            }

            else -> {
                backToMenuButton.checkAndActivate(x, y);
            }
        }
        for (buttons in gameButtons) buttons.down = false;
    }

    private fun draw() {
        if (holder.surface.isValid) {
            canvas = holder.lockCanvas();

            canvas.drawRect(
                0f, 0f, canvas.width * 1F,
                canvas.height * 1F, backgroundPaint
            );

            when (gameState) {
                GameStatus.MENU -> for (drawn in menuDrawers) drawn.draw(canvas);
                else -> drawGameLayout(canvas);
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }


    private fun drawGameLayout(canvas: Canvas?) {
        for (w in walls) w.draw(canvas);
        field.draw(canvas);
        for (drawn in gameDrawers) drawn.draw(canvas);
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
            80f, 20f, walls, plaques, finishPlaque, context
        );
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