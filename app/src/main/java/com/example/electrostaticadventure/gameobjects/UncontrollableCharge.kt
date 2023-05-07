package com.example.electrostaticadventure.gameobjects

import android.content.Context
import android.graphics.BitmapFactory
import com.example.electrostaticadventure.R
import com.example.electrostaticadventure.mathmodule.Vector2D

class UncontrollableCharge(polarity: Int, center: Vector2D, context: Context) : Charge(
    polarity, center,
    context
) {

    init {
        texture =
            if (polarity < 0) BitmapFactory.decodeResource(context.resources, R.drawable.ncs1);
            else BitmapFactory.decodeResource(context.resources, R.drawable.pcs2);
    }

}