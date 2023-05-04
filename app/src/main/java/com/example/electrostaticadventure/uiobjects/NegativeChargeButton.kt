package com.example.electrostaticadventure.uiobjects

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import com.example.electrostaticadventure.GameManager
import com.example.electrostaticadventure.gameobjects.CSCharge
import com.example.electrostaticadventure.gameobjects.Field

class NegativeChargeButton(
    frame: RectF, context: Context, pressedImageId: Int, idleImageId: Int,
    activeImageId: Int, gameManager: GameManager, var field: Field,
    var remainingCharges: ArrayList<CSCharge>
) : GameButton(
    frame,
    context, pressedImageId, idleImageId, activeImageId, gameManager
) {

    override fun sleepSetup() {}

    override fun activate() {}

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        // add text on top to display the amount of charge left.
    }
}