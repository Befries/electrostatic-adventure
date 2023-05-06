package com.example.electrostaticadventure.gameobjects.map

import com.example.electrostaticadventure.Drawer
import com.example.electrostaticadventure.mathmodule.Vector2D

class BlockLeftRight(
    vector: Vector2D, width: Float, height: Float, widthway: Float,
    widthwall: Float
) : Block(
    vector, width, height,
    widthway, widthwall
), Drawer {

    init {
        walls.add(
            WallBlock(
                Vector2D(vector.x,
                    vector.x + width),
                Vector2D(vector.y + height / 2 - widthway / 2,
                    vector.y + height / 2 - widthway / 2 + widthwall)
            )
        )
        walls.add(
            WallBlock(
                Vector2D(vector.x,
                    vector.x + width),
                Vector2D(vector.y + height / 2 + widthway / 2,
                    vector.y + height / 2 + widthway / 2 + widthwall)
            )
        )
    }
}