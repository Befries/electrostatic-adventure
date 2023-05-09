package com.example.electrostaticadventure.gameobjects.plaques

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF
import com.example.electrostaticadventure.R
import com.example.electrostaticadventure.gameobjects.Journeyer

class PolarityChangePlaque(rectPlaque: RectF, context: Context) : Plaque(rectPlaque) {

    private val textureUp =
        BitmapFactory.decodeResource(context.resources, R.drawable.polarity_change_up);
    private val textureDown =
        BitmapFactory.decodeResource(context.resources, R.drawable.polarity_change_down);

    override fun reaction(journeyer: Journeyer) {
        journeyer.polarity *= -1
    }

    override fun draw(canvas: Canvas?) {
        val texture = if (activated) textureUp
        else textureDown;
        canvas?.drawBitmap(texture, null, rectPlaque, null);
    }
}