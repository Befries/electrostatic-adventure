package com.example.electrostaticadventure.uiobjects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF
import com.example.electrostaticadventure.Drawer
import com.example.electrostaticadventure.GameManager

abstract class GameButton(
    protected val frame: RectF, context: Context,
    pressedImageId: Int, idleImageId: Int, activeImageId: Int,
    val gameManager: GameManager
) : Drawer {

    protected val idleTexture: Bitmap = BitmapFactory.decodeResource(context.resources, idleImageId);
    protected val activeTexture: Bitmap = BitmapFactory.decodeResource(context.resources, activeImageId);
    protected val pressedTexture: Bitmap = BitmapFactory.decodeResource(context.resources, pressedImageId);

    var active = false;
    var down = false;

    private final fun isIn(x: Float, y: Float): Boolean {
        return frame.contains(x, y);
    }

    open fun press(x: Float, y: Float) {
        if (isIn(x, y)) down = true;
    }

    final fun checkAndActivate(x: Float, y: Float) {
        down = false;
        if (!isIn(x, y)) return;
        active = true;
        activate();
    }

    public open fun toSleep() {
        active = false;
    }

    // action of the button when he goes to sleep

    // action of the button when he is pressed
    abstract fun activate();

    override fun draw(canvas: Canvas?) {
        if (down) canvas?.drawBitmap(pressedTexture, null, frame, null);
        else if (active) canvas?.drawBitmap(activeTexture, null, frame, null);
        else canvas?.drawBitmap(idleTexture, null, frame, null);
    }

}