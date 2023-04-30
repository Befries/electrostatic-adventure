package com.example.electrostaticadventure.gameobjects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Plaque (var rectPlaque: RectF) {

    private val paint = Paint();

    init {
        paint.color = Color.RED;
    }

    fun draw(canvas : Canvas?){
        canvas?.drawRect(rectPlaque, paint);
    }

    fun check(journeyer : Journeyer){
        if (rectPlaque.intersect(journeyer.hitBox)) journeyer.polarityChange();
    }
}