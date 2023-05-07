package com.example.electrostaticadventure.gameobjects.map

import com.example.electrostaticadventure.Drawer
import com.example.electrostaticadventure.mathmodule.Vector2D

class BlockDownLeft(
    vector: Vector2D, width: Float, height: Float, widthWay: Float,
    widthWall: Float
) : Block(
    vector, width, height,
    widthWay, widthWall
) , Drawer {
    init {
        walls.add(
            WallBlock(
                Vector2D(vector.x + width / 2 - widthWay / 2 - widthWall,
                    vector.x + width / 2 - widthWay / 2),
                Vector2D(vector.y + height / 2 + widthWay / 2,
                    vector.y + height)
            )
        )
        walls.add(
            WallBlock(
                Vector2D(vector.x + width / 2 + widthWay / 2,
                    vector.x + width / 2 + widthWay / 2 + widthWall),
                Vector2D(vector.y + height / 2 - widthWay / 2,
                    vector.y + height)
            )
        )
        walls.add(
            WallBlock(
                Vector2D(vector.x,
                    vector.x + width / 2 + widthWay / 2 + widthWall),
                Vector2D(vector.y + height / 2 - widthWay / 2,
                    vector.y + height / 2 - widthWay / 2+ widthWall)
            )
        )
        walls.add(
            WallBlock(
                Vector2D(vector.x,
                    vector.x + width / 2 - widthWay / 2 - widthWall),
                Vector2D(vector.y + height / 2 + widthWay / 2,
                    vector.y + height / 2 + widthWay / 2 + widthWall)
            )
        )
    }
}