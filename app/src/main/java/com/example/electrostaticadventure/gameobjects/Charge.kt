package com.example.electrostaticadventure.gameobjects

import android.content.Context
import com.example.electrostaticadventure.Drawer
import com.example.electrostaticadventure.GameManager
import com.example.electrostaticadventure.mathmodule.Vector2D
import com.example.electrostaticadventure.mathmodule.Vector2D.Companion.magSquared
import com.example.electrostaticadventure.mathmodule.Vector2D.Companion.normal

abstract class Charge(private val polarity: Int, var center: Vector2D, var context: Context): Drawer {

    public fun getFieldAt(pos: Vector2D): Vector2D {
        // if right on it, no field
        if (pos == center) return Vector2D(0f, 0f);

        // calculate the distance between and the unlimited Field
        val separation = (pos - center) / GameManager.scaleFactor;
        val uField = polarity.toFloat() * GameManager.strength / magSquared(separation);
        return normal(separation) * uField;
    }

}