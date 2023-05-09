package com.example.electrostaticadventure.gameobjects.map

import android.graphics.Canvas
import android.graphics.Color
import com.example.electrostaticadventure.Drawer
import com.example.electrostaticadventure.gameobjects.Journeyer
import com.example.electrostaticadventure.gameobjects.observer.Observer
import com.example.electrostaticadventure.mathmodule.Vector2D


// widthWay:
abstract class Block(
    val origin: Vector2D,
    val width: Float,
    val height: Float,
    val widthWay: Float,
    val widthWall: Float
) {
    var walls = ArrayList<WallBlock>()

    fun draw(canvas: Canvas?) {
        for (wall in walls) wall.draw(canvas);
    }

    fun check(journeyer: Journeyer, dt: Float) {
        for (wall in walls) wall.check(journeyer, dt);
    }

    fun update(journeyer: Journeyer) {
        for (wall in walls) wall.update(journeyer);
    }

    fun colorReset() {
        for (wall in walls) wall.colorReset()
    }
}