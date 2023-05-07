package com.example.electrostaticadventure.gameobjects.map

import android.graphics.Canvas
import com.example.electrostaticadventure.Drawer
import com.example.electrostaticadventure.gameobjects.Journeyer
import com.example.electrostaticadventure.mathmodule.Vector2D
import com.example.electrostaticadventure.gameobjects.map.BlockType.*
import com.example.electrostaticadventure.gameobjects.observer.Observer


class Map (val rows : Int,
           val columns : Int,
           val widthScreen : Float,
           val heightScreen : Float,
           val origin : Vector2D)
    : ArrayList<Block>(), Drawer, Observer {
    val blockSizeX = widthScreen.toFloat() / columns.toFloat()
    val blockSizeY = heightScreen.toFloat() / rows.toFloat()
    val startingPos = Vector2D(origin.x + 3.5f*blockSizeX, origin.y + 0.5f*blockSizeY)


    fun addBlock(numRowInt : Int, numColumnInt : Int, blockType : BlockType){
        val numRow = numRowInt.toFloat()
        val numColumn = numColumnInt.toFloat()
        when (blockType) {
            DownLeft -> this.add(BlockDownLeft(Vector2D(
                origin.x + numRow*blockSizeX,
                origin.y + numColumn*blockSizeY),
                blockSizeX,
                blockSizeY,
                blockSizeX/2,
                25f))
            DownRight -> this.add(BlockDownRight(Vector2D(
                origin.x + numRow*blockSizeX,
                origin.y + numColumn*blockSizeY),
                blockSizeX,
                blockSizeY,
                blockSizeX/2,
                25f))
            LeftRight -> this.add(BlockLeftRight(Vector2D(
                origin.x + numRow*blockSizeX,
                origin.y + numColumn*blockSizeY),
                blockSizeX,
                blockSizeY,
                blockSizeX/2,
                25f))
            TopDown -> this.add(BlockTopDown(Vector2D(
                origin.x + numRow*blockSizeX,
                origin.y + numColumn*blockSizeY),
                blockSizeX,
                blockSizeY,
                blockSizeX/2,
                25f))
            TopLeft -> this.add(BlockTopLeft(Vector2D(
                origin.x + numRow*blockSizeX,
                origin.y + numColumn*blockSizeY),
                blockSizeX,
                blockSizeY,
                blockSizeX/2,
                25f))
            TopRight -> this.add(BlockTopRight(Vector2D(
                origin.x + numRow*blockSizeX,
                origin.y + numColumn*blockSizeY),
                blockSizeX,
                blockSizeY,
                blockSizeX/2,
                25f))

        }
    }

    override fun draw(canvas: Canvas?){
        for (block in this) block .draw(canvas)
    }

    fun check(journeyer: Journeyer, dt : Float){
        for (block in this) block.check(journeyer, dt)
    }

    override fun update(journeyer : Journeyer){
        for (block in this) block.update(journeyer)
    }

    fun reset(){
        for (block in this) block.colorReset()
    }
}