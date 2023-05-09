package com.example.electrostaticadventure.gameobjects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import com.example.electrostaticadventure.Drawer
import com.example.electrostaticadventure.GameManager
import com.example.electrostaticadventure.mathmodule.Vector2D
import com.example.electrostaticadventure.mathmodule.Vector2D.Companion.magSquared
import com.example.electrostaticadventure.mathmodule.Vector2D.Companion.normal

abstract class Charge(val polarity: Int, var center: Vector2D, var context: Context) : Drawer {

    protected lateinit var texture: Bitmap;
    private val radius = 50;
    var hitbox = RectF(
        center.x - radius, center.y - radius,
        center.x + radius, center.y + radius
    );


    public fun getFieldAt(pos: Vector2D): Vector2D {
        // if right on it, no field
        if (pos == center) return Vector2D(0f, 0f);

        // calculate the distance between and the unlimited Field
        val separation = (pos - center) / GameManager.scaleFactor;
        val uField = polarity.toFloat() * GameManager.strength / magSquared(separation);
        return normal(separation) * uField;
    }

    override fun draw(canvas: Canvas?) {
        canvas?.drawBitmap(texture, null, hitbox, null)
    }

}