package com.example.electrostaticadventure.gameobjects.plaques

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.example.electrostaticadventure.Drawer
import com.example.electrostaticadventure.gameobjects.Journeyer

abstract class Plaque (var rectPlaque: RectF) : Drawer {

    private val paint = Paint();
    private var polarityChange = "possible"

    init {
        paint.color = Color.RED;
    }

    override fun draw(canvas : Canvas?){
        canvas?.drawRect(rectPlaque, paint);
    }

    fun check(journeyer : Journeyer){
        if (rectPlaque.intersect(journeyer.hitBox) && polarityChange == "possible") {
            reaction(journeyer)
            polarityChange = "none"
        };
    }

    abstract fun reaction(journeyer: Journeyer);
}