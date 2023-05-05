package com.example.electrostaticadventure.gameobjects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.example.electrostaticadventure.R
import com.example.electrostaticadventure.mathmodule.Vector2D

class UncontrolableCharge(polarity: Int, center: Vector2D, context: Context) : Charge(
    polarity, center,
    context
) {

    private val radius = 60;
    private var texture: Bitmap;
    var hitbox = RectF(
        center.x - radius, center.y - radius,
        center.x + radius, center.y + radius
    );

    init {
        texture =
            if (polarity < 0) BitmapFactory.decodeResource(context.resources, R.drawable.ncs1);
            else BitmapFactory.decodeResource(context.resources, R.drawable.pcs2);
    }

    override fun draw(canvas: Canvas?) {
        canvas?.drawBitmap(texture, null, hitbox, null)
    }
}