package com.example.electrostaticadventure.gameobjects.map

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.example.electrostaticadventure.Drawer
import com.example.electrostaticadventure.gameobjects.Journeyer
import com.example.electrostaticadventure.mathmodule.Vector2D
import kotlin.math.abs

class WallBlock(topLeft: Vector2D, downRight: Vector2D) : Drawer {
    private val hitBox = RectF(topLeft.x, topLeft.y, downRight.x, downRight.y);
    private val paint = Paint()
    private var orientation = hitBox.width() > hitBox.height();

    private val initialColor = Color.RED

    init {
        paint.color = initialColor
    }

    override fun draw(canvas: Canvas?) {
        canvas?.drawRect(hitBox, paint)
    }

    fun check(journeyer: Journeyer, dt: Float) {
        if (RectF.intersects(journeyer.hitBox, hitBox)) {
            val intersection = RectF(hitBox);
            intersection.intersect(journeyer.hitBox);
            if (orientation) {
                if (intersection.width() > intersection.height()) journeyer.bounce(orientation, dt);
                else journeyer.bounce(!orientation, dt);
            } else {
                if (intersection.height() > intersection.width()) journeyer.bounce(orientation, dt);
                else journeyer.bounce(!orientation, dt);
            }
        };
    }

    fun update(journeyer: Journeyer) {
        if (journeyer.polarity == -1f) paint.color = Color.BLUE
        else paint.color = initialColor

    }

    fun colorReset() {
        paint.color = initialColor
    }

}