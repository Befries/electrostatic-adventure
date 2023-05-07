package com.example.electrostaticadventure.gameobjects.plaques

import android.graphics.RectF
import com.example.electrostaticadventure.gameobjects.Journeyer

class PolarityChangePlaque(rectPlaque: RectF) : Plaque(rectPlaque) {
    override fun reaction(journeyer: Journeyer) {
        journeyer.polarity *= -1
    }
}