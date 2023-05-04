package com.example.electrostaticadventure.gameobjects.plaques

import android.graphics.RectF
import com.example.electrostaticadventure.GameManager
import com.example.electrostaticadventure.gameobjects.Journeyer

class FinishPlaque(rectPlaque: RectF, val gameManager: GameManager) : Plaque(rectPlaque) {
    override fun reaction(journeyer: Journeyer) {
        // finish the game, interact with the gameManager
    }
}