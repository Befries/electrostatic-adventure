package com.example.electrostaticadventure.gameobjects.plaques

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.example.electrostaticadventure.Drawer
import com.example.electrostaticadventure.gameobjects.Journeyer

abstract class Plaque (var rectPlaque: RectF) : Drawer {

    private val paint = Paint();
    var polarityChangeState = "possible"

    init {
        paint.color = Color.RED;
    }

    override fun draw(canvas : Canvas?){
        canvas?.drawRect(rectPlaque, paint);
    }

    fun check(journeyer : Journeyer){
        if (RectF.intersects(journeyer.hitBox, rectPlaque) && polarityChangeState == "possible") {
            reaction(journeyer)
            polarityChangeState = "none"
        };
    }

    abstract fun reaction(journeyer: Journeyer);
}