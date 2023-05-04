package com.example.electrostaticadventure.uiobjects

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import com.example.electrostaticadventure.GameManager
import com.example.electrostaticadventure.gameobjects.ControlableCharge
import com.example.electrostaticadventure.gameobjects.Field
import com.example.electrostaticadventure.gameobjects.plaques.EditorMode

class ChargeButton(
    frame: RectF, context: Context, pressedImageId: Int, idleImageId: Int,
    activeImageId: Int, gameManager: GameManager, editorMode: EditorMode
) : GameButton(
    frame,
    context, pressedImageId, idleImageId, activeImageId, gameManager
) {

    override fun sleepSetup() {

    }

    override fun activate() {

    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        // add text on top to display the amount of charge left.
    }
}