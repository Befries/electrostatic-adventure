package com.example.electrostaticadventure.gameobjects.observer

import com.example.electrostaticadventure.gameobjects.Journeyer

interface Observer {
    fun update(journeyer: Journeyer)
}