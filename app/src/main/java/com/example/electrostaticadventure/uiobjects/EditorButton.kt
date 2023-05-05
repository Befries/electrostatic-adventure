package com.example.electrostaticadventure.uiobjects

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import com.example.electrostaticadventure.GameManager
import com.example.electrostaticadventure.gameobjects.plaques.EditorMode

class EditorButton(
    frame: RectF,
    context: Context,
    pressedImageId: Int,
    idleImageId: Int,
    activeImageId: Int,
    gameManager: GameManager,
    private val editorMode: EditorMode,
) : GameButton(
    frame,
    context, pressedImageId, idleImageId, activeImageId, gameManager
) {


    override fun sleepSetup() {
        gameManager.editorMode = EditorMode.IDLE;
    }

    override fun activate() {
        if (gameManager.editorMode != editorMode) {
            for (editor in gameManager.editorButton) if (editor != this) editor.toSleep();
            gameManager.editorMode = editorMode
        };
        else toSleep();
    }

}