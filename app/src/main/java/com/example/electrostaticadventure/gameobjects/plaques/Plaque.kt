package com.example.electrostaticadventure.gameobjects.plaques

import android.graphics.RectF
import com.example.electrostaticadventure.Drawer
import com.example.electrostaticadventure.gameobjects.Journeyer

abstract class Plaque(protected var rectPlaque: RectF) : Drawer {

    var activated = true;

    fun check(journeyer: Journeyer) {
        if (RectF.intersects(journeyer.hitBox, rectPlaque) && activated) {
            reaction(journeyer);
            activated = false;
        };
    }

    abstract fun reaction(journeyer: Journeyer);
}