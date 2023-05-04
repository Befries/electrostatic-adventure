package com.example.electrostaticadventure.uiobjects

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF
import com.example.electrostaticadventure.Drawer
import com.example.electrostaticadventure.GameManager

abstract class GameButton(
    private val frame: RectF, context: Context,
    pressedImageId: Int, idleImageId: Int, activeImageId: Int,
    val gameManager: GameManager
) : Drawer {

    private val idleImage = BitmapFactory.decodeResource(context.resources, idleImageId);
    private val activeImage = BitmapFactory.decodeResource(context.resources, activeImageId);
    private val pressedImage = BitmapFactory.decodeResource(context.resources, pressedImageId);

    var active = false;
    var down = false;

    private final fun isIn(x: Float, y: Float): Boolean {
        return frame.contains(x, y);
    }

    final fun press(x: Float, y: Float) {
        if (isIn(x, y)) down = true;
    }

    final fun checkAndActivate(x: Float, y: Float) {
        val interaction = isIn(x, y);
        if (!interaction || active) return;
        active = true;
        activate();
    }

    public fun toSleep() {
        active = false;
        sleepSetup();
    }

    // action of the button when he goes to sleep
    abstract fun sleepSetup();

    // action of the button when he is pressed
    abstract fun activate();

    override fun draw(canvas: Canvas?) {
        if (down) canvas?.drawBitmap(pressedImage, null, frame, null);
        else if (active) canvas?.drawBitmap(activeImage, null, frame, null);
        else canvas?.drawBitmap(idleImage, null, frame, null);
    }

}