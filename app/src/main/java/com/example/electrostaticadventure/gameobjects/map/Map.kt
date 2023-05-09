package com.example.electrostaticadventure.gameobjects.map

import android.graphics.Canvas
import android.graphics.RectF
import com.example.electrostaticadventure.Drawer
import com.example.electrostaticadventure.gameobjects.Journeyer
import com.example.electrostaticadventure.mathmodule.Vector2D
import com.example.electrostaticadventure.gameobjects.map.BlockType.*
import com.example.electrostaticadventure.gameobjects.observer.Observer
import com.example.electrostaticadventure.gameobjects.plaques.Plaque


class Map(
    private val rows: Int,
    private val columns: Int,
    private val widthScreen: Float,
    private val heightScreen: Float,
    private val origin: Vector2D
) : ArrayList<Block>(), Drawer, Observer {
    val lonelyWalls = ArrayList<WallBlock>();
    private val blockSizeX = widthScreen / columns.toFloat()
    private val blockSizeY = heightScreen / rows.toFloat()
    private val wallWidth = 20f;
    private val corridorWidth = 3 * blockSizeX / 4;
    val startingPos = Vector2D(origin.x + 3.5f * blockSizeX, origin.y + 0.5f * blockSizeY)


    fun addBlock(numRowInt: Int, numColumnInt: Int, blockType: BlockType) {
        val numRow = numRowInt.toFloat()
        val numColumn = numColumnInt.toFloat()

        when (blockType) {
            DownLeft -> this.add(
                BlockDownLeft(
                    Vector2D(
                        origin.x + numRow * blockSizeX,
                        origin.y + numColumn * blockSizeY
                    ),
                    blockSizeX,
                    blockSizeY,
                    corridorWidth,
                    wallWidth
                )
            )

            DownRight -> this.add(
                BlockDownRight(
                    Vector2D(
                        origin.x + numRow * blockSizeX,
                        origin.y + numColumn * blockSizeY
                    ),
                    blockSizeX,
                    blockSizeY,
                    corridorWidth,
                    wallWidth
                )
            )

            LeftRight -> this.add(
                BlockLeftRight(
                    Vector2D(
                        origin.x + numRow * blockSizeX,
                        origin.y + numColumn * blockSizeY
                    ),
                    blockSizeX,
                    blockSizeY,
                    corridorWidth,
                    wallWidth
                )
            )

            TopDown -> this.add(
                BlockTopDown(
                    Vector2D(
                        origin.x + numRow * blockSizeX,
                        origin.y + numColumn * blockSizeY
                    ),
                    blockSizeX,
                    blockSizeY,
                    corridorWidth,
                    wallWidth
                )
            )

            TopLeft -> this.add(
                BlockTopLeft(
                    Vector2D(
                        origin.x + numRow * blockSizeX,
                        origin.y + numColumn * blockSizeY
                    ),
                    blockSizeX,
                    blockSizeY,
                    corridorWidth,
                    wallWidth
                )
            )

            TopRight -> this.add(
                BlockTopRight(
                    Vector2D(
                        origin.x + numRow * blockSizeX,
                        origin.y + numColumn * blockSizeY
                    ),
                    blockSizeX,
                    blockSizeY,
                    corridorWidth,
                    wallWidth
                )
            )

        }
    }


    fun getRectF(numRowInt: Int, numColumnInt: Int): RectF {
        val numColumn = numColumnInt.toFloat()
        val numRow = numRowInt.toFloat()
        return RectF(
            origin.x + numColumn * blockSizeX,
            origin.y + numRow * blockSizeY,
            origin.x + (numColumn + 1f) * blockSizeX,
            origin.y + (numRow + 1f) * blockSizeY
        )
    }

    fun getInnerRectF(numRowInt: Int, numColumnInt: Int): RectF {
        val numColumn = numColumnInt.toFloat()
        val numRow = numRowInt.toFloat()
        return RectF(
            origin.x + (numColumn + 1f / 2) * blockSizeX - corridorWidth / 2,
            origin.y + (numRow + 1f / 2) * blockSizeY - corridorWidth / 2,
            origin.x + (numColumn + 1f / 2) * blockSizeX + corridorWidth / 2,
            origin.y + (numRow + 1f / 2) * blockSizeY + corridorWidth / 2
        )
    }

    override fun draw(canvas: Canvas?) {
        for (block in this) block.draw(canvas);
        for (wallBlock in lonelyWalls) wallBlock.draw(canvas);
    }

    fun check(journeyer: Journeyer, dt: Float) {
        for (block in this) block.check(journeyer, dt)
        for (wallBlock in lonelyWalls) wallBlock.check(journeyer, dt);
    }

    override fun update(journeyer: Journeyer) {
        for (block in this) block.update(journeyer)
        for (wallBlock in lonelyWalls) wallBlock.update(journeyer);
    }

    fun reset() {
        for (block in this) block.colorReset()
        for (wallBlock in lonelyWalls) wallBlock.colorReset();
    }
}