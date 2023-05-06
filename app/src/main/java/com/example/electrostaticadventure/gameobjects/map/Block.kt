package com.example.electrostaticadventure.gameobjects.map

import android.graphics.Canvas
import com.example.electrostaticadventure.Drawer
import com.example.electrostaticadventure.gameobjects.Journeyer
import com.example.electrostaticadventure.mathmodule.Vector2D


abstract class Block(
    val origin: Vector2D,
    val width: Float,
    val height: Float,
    val widthway: Float,
    val widthwall: Float
) : Drawer {
    var walls = ArrayList<WallBlock>()

    override fun draw(canvas: Canvas?) {
        for (wall in walls) {
            wall.draw(canvas)
        }

    }
    fun check(journeyer : Journeyer, dt : Float){
        for (wall in walls) wall.check(journeyer, dt)
    }
}