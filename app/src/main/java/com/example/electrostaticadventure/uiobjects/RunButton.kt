package com.example.electrostaticadventure.uiobjects

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF
import com.example.electrostaticadventure.GameManager
import com.example.electrostaticadventure.GameStatus

class RunButton(frame: RectF, context: Context, pressedImageId: Int, idleImageId: Int,
                activeImageId: Int, pressedActiveImageId: Int, gameManager: GameManager
) : GameButton(frame, context,
    pressedImageId,
    idleImageId, activeImageId, gameManager
) {

    private val pressedStopTexture = BitmapFactory.decodeResource(context.resources, pressedActiveImageId);

    override fun sleepSetup() {
        gameManager.gameState = GameStatus.DEPLOYMENT;
    }

    override fun activate() {
        if (gameManager.gameState == GameStatus.RUNNING) toSleep();
        else gameManager.runGame();
    }

    override fun draw(canvas: Canvas?) {
        if (down) {
            if (active) canvas?.drawBitmap(pressedStopTexture, null, frame, null);
            else canvas?.drawBitmap(pressedTexture, null, frame, null);
        } else {
            if (active) canvas?.drawBitmap(activeTexture, null, frame, null);
            else canvas?.drawBitmap(idleTexture, null, frame, null);
        }
    }

}