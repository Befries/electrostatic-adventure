package com.example.electrostaticadventure.uiobjects

import android.content.Context
import android.graphics.RectF
import com.example.electrostaticadventure.GameManager
import com.example.electrostaticadventure.GameStatus

class LaunchButton(
    frame: RectF, context: Context, pressedImageId:
    Int, idleImageId: Int, activeImageId: Int, gameManager: GameManager
) : GameButton(frame, context, pressedImageId, idleImageId, activeImageId, gameManager) {


    override fun activate() {
        gameManager.gameState = GameStatus.DEPLOYMENT;
        toSleep();
    }

    override fun sleepSetup() {}

}