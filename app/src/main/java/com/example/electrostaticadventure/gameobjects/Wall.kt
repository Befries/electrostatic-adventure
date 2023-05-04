package com.example.electrostaticadventure.gameobjects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.example.electrostaticadventure.Drawer
import com.example.electrostaticadventure.mathmodule.Vector2D
import kotlin.math.abs

class Wall(topLeft: Vector2D, downRight: Vector2D): Drawer {

    private var hitBox = RectF(topLeft.x, topLeft.y, downRight.x, downRight.y);
    private var orientation = abs(topLeft.x - downRight.x) > abs(topLeft.y - downRight.y);
    private var paint = Paint();

    init {
        paint.color = Color.GRAY;
    }

    fun check(journeyer: Journeyer, dt: Float) {
        if (RectF.intersects(journeyer.hitBox, hitBox)) journeyer.bounce(orientation, dt);
    }

    override fun draw(canvas: Canvas?) {
        canvas?.drawRect(hitBox, paint);
    }

}