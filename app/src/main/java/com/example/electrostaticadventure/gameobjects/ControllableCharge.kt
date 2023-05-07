package com.example.electrostaticadventure.gameobjects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF
import com.example.electrostaticadventure.R
import com.example.electrostaticadventure.mathmodule.Vector2D

class ControllableCharge(polarity: Int, center: Vector2D, context: Context) : Charge(
    polarity, center,
    context
) {

    init {
        texture =
            if (polarity < 0) BitmapFactory.decodeResource(context.resources, R.drawable.controlable_negative);
            else BitmapFactory.decodeResource(context.resources, R.drawable.controlable_positive);
    }

}