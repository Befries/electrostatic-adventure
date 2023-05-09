package com.example.electrostaticadventure.gameobjects.map

import com.example.electrostaticadventure.Drawer
import com.example.electrostaticadventure.mathmodule.Vector2D

class BlockTopRight(
    vector: Vector2D, width: Float, height: Float, widthway: Float,
    widthwall: Float
) : Block(
    vector, width, height,
    widthway, widthwall
), Drawer {
    init {
        walls.add(
            WallBlock(
                Vector2D(
                    vector.x + width / 2 - widthway / 2 - widthwall,
                    vector.y
                ),
                Vector2D(
                    vector.x + width / 2 - widthway / 2,
                    vector.y + height / 2 + widthway / 2 + widthwall
                )
            )
        )
        walls.add(
            WallBlock(
                Vector2D(
                    vector.x + width / 2 + widthway / 2,
                    vector.y
                ),
                Vector2D(
                    vector.x + width / 2 + widthway / 2 + widthwall,
                    vector.y + height / 2 - widthway / 2
                )
            )
        )
        walls.add(
            WallBlock(
                Vector2D(
                    vector.x + width / 2 - widthway / 2,
                    vector.y + height / 2 + widthway / 2
                ),
                Vector2D(
                    vector.x + width,
                    vector.y + height / 2 + widthway / 2 + widthwall
                )
            )
        )
        walls.add(
            WallBlock(
                Vector2D(
                    vector.x + width / 2 + widthway / 2,
                    vector.y + height / 2 - widthway / 2
                ),
                Vector2D(
                    vector.x + width,
                    vector.y + height / 2 - widthway / 2 + widthwall
                )
            )
        )
    }
}