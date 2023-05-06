package com.example.electrostaticadventure.gameobjects.map

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.example.electrostaticadventure.Drawer
import com.example.electrostaticadventure.gameobjects.Journeyer
import com.example.electrostaticadventure.mathmodule.Vector2D
import kotlin.math.abs

class WallBlock(x : Vector2D, y : Vector2D) : Drawer {
    private val hitBox = RectF(x.x, y.x, x.y, y.y)
    val paint = Paint()
    private var orientation = abs(x.x - x.y) > abs(y.x - y.y)

    override fun draw(canvas: Canvas?) {
        paint.color = Color.LTGRAY
        canvas?.drawRect(hitBox, paint)
    }

    fun check(journeyer: Journeyer, dt: Float) {
        if (RectF.intersects(journeyer.hitBox, hitBox)) journeyer.bounce(orientation, dt);
    }


}