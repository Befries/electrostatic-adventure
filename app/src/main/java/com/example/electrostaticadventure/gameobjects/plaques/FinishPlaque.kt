package com.example.electrostaticadventure.gameobjects.plaques

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.example.electrostaticadventure.GameManager
import com.example.electrostaticadventure.R
import com.example.electrostaticadventure.gameobjects.Journeyer

class FinishPlaque(rectPlaque: RectF, private val gameManager: GameManager) :
    Plaque(rectPlaque) {

    private val texture =
        BitmapFactory.decodeResource(gameManager.context.resources, R.drawable.finish_plaque);

    override fun reaction(journeyer: Journeyer) {
        gameManager.endGame();
    }

    override fun draw(canvas: Canvas?) {
        canvas?.drawBitmap(texture, null, rectPlaque, null);
    }
}