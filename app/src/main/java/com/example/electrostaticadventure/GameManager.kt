package com.example.electrostaticadventure

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.electrostaticadventure.GameStatus.*
import com.example.electrostaticadventure.gameobjects.Charge
import com.example.electrostaticadventure.gameobjects.ControllableCharge
import com.example.electrostaticadventure.gameobjects.Field
import com.example.electrostaticadventure.gameobjects.Journeyer
import com.example.electrostaticadventure.gameobjects.UncontrollableCharge
import com.example.electrostaticadventure.uiobjects.EditorMode.*
import com.example.electrostaticadventure.gameobjects.plaques.FinishPlaque
import com.example.electrostaticadventure.gameobjects.plaques.Plaque
import com.example.electrostaticadventure.mathmodule.Vector2D
import com.example.electrostaticadventure.uiobjects.EditorButton
import com.example.electrostaticadventure.uiobjects.GameButton
import com.example.electrostaticadventure.uiobjects.GameCounter
import com.example.electrostaticadventure.uiobjects.LaunchButton
import com.example.electrostaticadventure.uiobjects.MenuButton
import com.example.electrostaticadventure.uiobjects.RunButton
import com.example.electrostaticadventure.gameobjects.plaques.PolarityChangePlaque
import com.example.electrostaticadventure.gameobjects.map.Map
import com.example.electrostaticadventure.gameobjects.map.BlockType.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import android.content.DialogInterface
import android.app.AlertDialog
import android.app.Dialog
import com.example.electrostaticadventure.gameobjects.map.WallBlock

class GameManager @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attributes, defStyleAttr), SurfaceHolder.Callback, Runnable {

    companion object {
        const val maxTotalField = 100f;
        const val strength = 50f;
        const val scaleFactor = 500f;
        const val timeAcceleration = 2f;
    }

    private val activity = context as FragmentActivity

    private lateinit var playgroundArea: RectF;

    private var menuDrawers = ArrayList<Drawer>();
    private var gameDrawers = ArrayList<Drawer>();

    var gameState = MENU;
    var editorMode = IDLE;

    private lateinit var menuButtons: Array<GameButton>;
    private lateinit var gameButtons: Array<GameButton>;
    public lateinit var editorButton: Array<EditorButton>;
    private lateinit var runStopButton: RunButton;

    private var field = Field(30, 10);
    private lateinit var finishPlaque: FinishPlaque;
    private var outerWalls = ArrayList<WallBlock>();
    private var plaques = ArrayList<Plaque>();

    private lateinit var negativeCounter: GameCounter
    private lateinit var positiveCounter: GameCounter

    private var map = Map(0, 0, 0f, 0f, Vector2D(0f, 0f))

    lateinit var thread: Thread;
    lateinit var canvas: Canvas;

    private var drawing = true;
    private var environmentInitialized = false;

    lateinit var journeyer: Journeyer;

    private var backgroundPaint = Paint();

    init {
        backgroundPaint.color = Color.rgb(0, 39, 102);
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh);
        /*
         define where the game is played understanding that the top and bottom
         are occupied by buttons
         */

        val playgroundOrigin = Vector2D(0f, 5 * w.toFloat() / 32);
        val playgroundHeight = h.toFloat() - w.toFloat() / 4;

        playgroundArea = RectF(
            playgroundOrigin.x, playgroundOrigin.y,
            w.toFloat(),
            playgroundHeight
        );


        generateMenu(w.toFloat(), h.toFloat());
        generateGameLayout(w.toFloat(), playgroundHeight - playgroundOrigin.y, playgroundOrigin);
        generateGameUI(w.toFloat(), h.toFloat());
        environmentInitialized = true;
    }


    private fun generateMenu(w: Float, h: Float) {
        // length = two times height -> length = 6w/8 -> height = 3w/8
        val launchButton = LaunchButton(
            RectF(w / 8, h / 8, 7 * w / 8, (h + 3 * w) / 8),
            context, R.drawable.launch_button_down, R.drawable.launch_button_up,
            R.drawable.launch_button_down, this
        );

        menuDrawers.add(launchButton);
        menuButtons = arrayOf(launchButton);
    }

    private fun generateGameUI(w: Float, h: Float) {
        // length * 40 = height * 64 -> length = w/4 -> height = 5*w/32
        val backToMenuButton = MenuButton(
            RectF(0f, 0f, w / 4, 5 * w / 32),
            context,
            R.drawable.menu_button_down,
            R.drawable.menu_button_up,
            R.drawable.menu_button_down,
            this
        );

        runStopButton = RunButton(
            RectF(3 * w / 4, 0f, w, 5 * w / 32),
            context,
            R.drawable.run_button_down,
            R.drawable.run_button_up,
            R.drawable.stop_button_up,
            R.drawable.stop_button_down,
            this
        );

        val positiveAdder = EditorButton(
            RectF(0f, h - w / 4, w / 3, h),
            context,
            R.drawable.add_positive_down,
            R.drawable.add_positive_up,
            R.drawable.add_positive_active,
            this, POSITIVE_CHARGES
        );

        val negativeAdder = EditorButton(
            RectF(w / 3, h - w / 4, 2 * w / 3, h),
            context,
            R.drawable.add_negative_down,
            R.drawable.add_negative_up,
            R.drawable.add_negative_active,
            this, NEGATIVE_CHARGES
        );

        val eraserButton = EditorButton(
            RectF(2 * w / 3, h - w / 4, w, h),
            context,
            R.drawable.erase_button_down,
            R.drawable.erase_button_up,
            R.drawable.erased_button_active,
            this, ERASER
        );

        gameButtons = arrayOf(
            backToMenuButton, runStopButton, positiveAdder, negativeAdder, eraserButton
        );
        editorButton = arrayOf(
            positiveAdder, negativeAdder, eraserButton
        )
        gameDrawers.addAll(gameButtons);

        // gotta add the counter to know how many charge are left to be placed.
        positiveCounter = GameCounter(w / 5, h - w / 16, 3);
        negativeCounter = GameCounter(w / 3 + 33 * w / 160, h - w / 16, 3);
        gameDrawers.add(positiveCounter);
        gameDrawers.add(negativeCounter);

    }

    private fun generateGameLayout(w: Float, h: Float, origin: Vector2D) {
        field.generateCellArray(w.toInt(), h.toInt(), origin);
        field.add(UncontrollableCharge(1, Vector2D(w / 2, h / 4), context));
        field.add(UncontrollableCharge(-1, Vector2D(w / 2, h * 3 / 4), context));

        map = Map(5, 4, w, h, origin)

        outerWalls.add(WallBlock(origin, origin + Vector2D(w, 40f)));
        outerWalls.add(WallBlock(origin + Vector2D(w - 40f, 0f), origin + Vector2D(w, h)));
        outerWalls.add(WallBlock(origin + Vector2D(0f, 0f), origin + Vector2D(40f, h)));
        outerWalls.add(WallBlock(origin + Vector2D(0f, h - 40f), origin + Vector2D(w, h)));

        map.lonelyWalls.addAll(outerWalls);

        map.addBlock(3, 0, LeftRight)
        map.addBlock(2, 0, DownRight)
        map.addBlock(2, 1, TopLeft)
        map.addBlock(1, 1, LeftRight)
        map.addBlock(0, 1, DownRight)
        map.addBlock(0, 2, TopRight)
        map.addBlock(1, 2, LeftRight)
        map.addBlock(2, 2, DownLeft)
        map.addBlock(2, 3, TopRight)
        map.addBlock(3, 3, DownLeft)
        map.addBlock(3, 4, TopLeft)
        map.addBlock(2, 4, LeftRight)
        map.addBlock(1, 4, TopRight)
        map.addBlock(1, 3, DownLeft)
        map.addBlock(0, 3, DownRight)
        map.addBlock(0, 4, TopLeft)


        plaques.add(PolarityChangePlaque(map.getInnerRectF(2, 2), context))
        plaques.add(PolarityChangePlaque(map.getInnerRectF(3, 1), context))

        finishPlaque = FinishPlaque(map.getInnerRectF(4, 0), this);
        plaques.add(finishPlaque);

        gameDrawers.addAll(plaques);
        gameDrawers.add(map);
        gameDrawers.add(field);
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
            MENU -> for (button in menuButtons) button.press(x, y);

            DEPLOYMENT -> {
                if (playgroundArea.contains(x, y)) gameInteraction(x, y);
                for (button in gameButtons) button.press(x, y);
            }

            RUNNING -> runStopButton.press(x, y);
        }
    }

    private fun gameInteraction(x: Float, y: Float) {
        when (editorMode) {
            NEGATIVE_CHARGES -> if (negativeCounter.value > 0) {
                field.add(ControllableCharge(-1, Vector2D(x, y), context));
                negativeCounter.value -= 1;
            };
            POSITIVE_CHARGES -> if (positiveCounter.value > 0) {
                field.add(ControllableCharge(1, Vector2D(x, y), context));
                positiveCounter.value -= 1;
            }

            ERASER -> {
                val removedCharge = ArrayList<Charge>();
                for (charge in field) {
                    // verify that the charge is controllable and inside the radius of delete
                    if (charge is ControllableCharge &&
                        Vector2D.mag(charge.center - Vector2D(x, y)) < 50
                    ) {
                        if (charge.polarity > 0) positiveCounter.value += 1;
                        else negativeCounter.value += 1;
                        removedCharge.add(charge);
                    }
                }
                field.removeAll(removedCharge.toSet());
            }

            IDLE -> {}
        }
    }


    private fun onTouchUP(x: Float, y: Float) {
        when (gameState) {
            MENU -> for (button in menuButtons) button.checkAndActivate(x, y);

            DEPLOYMENT -> for (button in gameButtons) button.checkAndActivate(x, y);

            RUNNING -> runStopButton.checkAndActivate(x, y);
        }

    }

    private fun draw() {
        if (holder.surface.isValid) {
            canvas = holder.lockCanvas();

            canvas.drawRect(
                0f, 0f, canvas.width * 1F,
                canvas.height * 1F, backgroundPaint
            );

            when (gameState) {
                MENU -> for (drawn in menuDrawers) drawn.draw(canvas);
                else -> drawGameLayout(canvas);
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }


    private fun drawGameLayout(canvas: Canvas?) {
        for (drawn in gameDrawers) drawn.draw(canvas);
        if (gameState == RUNNING) journeyer.draw(canvas);
        //if (gameState == RUNNING || gameState == DEPLOYMENT) journeyer.draw(canvas);
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
        var previousFrameTime = System.currentTimeMillis();

        while (drawing) {
            // calculate time passed between frame
            val currentFrameTime = System.currentTimeMillis();
            val delayTime =
                (currentFrameTime - previousFrameTime).toFloat() / 1000 * timeAcceleration;

            if (!environmentInitialized) continue;
            if (gameState == RUNNING) journeyer.update(delayTime);
            draw();
            previousFrameTime = currentFrameTime;
        }
    }

    public fun runGame() {
        journeyerReset()
        plaquesReset()
        gameState = RUNNING;
    }

    private fun journeyerReset() {
        journeyer = Journeyer(
            field, map.startingPos,
            40f, 20f, map, plaques, finishPlaque, context
        )
    }

    private fun fieldReset() {
        val removedCharge = ArrayList<Charge>()
        for (charge in field) {
            if (charge is ControllableCharge) {
                if (charge.polarity > 0) positiveCounter.value += 1
                else negativeCounter.value += 1
                removedCharge.add(charge)
            }
        }
        field.removeAll(removedCharge.toSet())
    }

    private fun plaquesReset() {
        for (plaque in plaques) plaque.activated = true;
    }

    private fun wallBlockReset() {
        map.reset()
    }


    fun gameMenuReset() {
        journeyerReset()
        plaquesReset()
        fieldReset()
        wallBlockReset()
    }

    fun gameStopReset() {
        journeyerReset()
        plaquesReset()
        wallBlockReset()
    }

    fun endGame() {
        drawing = false
        showGameOverDialog(R.string.end)
    }

    fun showGameOverDialog(messageId: Int) {
        class GameResult : DialogFragment() {
            override fun onCreateDialog(bundle: Bundle?): Dialog {
                val builder = AlertDialog.Builder(activity)
                builder.setTitle(resources.getString(messageId))
                //builder.setMessage(resources.getString(R.string.results_format))
                builder.setPositiveButton(R.string.reset_game,
                    DialogInterface.OnClickListener { _, _ -> newGame() }
                )
                return builder.create()
            }
        }

        activity.runOnUiThread(
            Runnable {
                val ft = activity.supportFragmentManager.beginTransaction()
                val prev =
                    activity.supportFragmentManager.findFragmentByTag("dialog")
                if (prev != null) {
                    ft.remove(prev)
                }
                ft.addToBackStack(null)
                val gameResult = GameResult()
                gameResult.isCancelable = false
                gameResult.show(ft, "dialog")
            }
        )
    }

    fun newGame() {
        drawing = true
        //environmentInitialized = false
        gameMenuReset()
        thread = Thread(this)
        thread.start()
    }


    override fun surfaceCreated(p0: SurfaceHolder) {
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {}

    override fun surfaceDestroyed(p0: SurfaceHolder) {}
}