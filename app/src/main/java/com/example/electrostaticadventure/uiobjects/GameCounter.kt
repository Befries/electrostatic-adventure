package com.example.electrostaticadventure.uiobjects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.electrostaticadventure.Drawer

class GameCounter(private val left: Float, private val top: Float, var value: Int) : Drawer {

    public val paint = Paint();

    init {
        paint.color = Color.BLACK;
        paint.textSize = 200f;
    }

    override fun draw(canvas: Canvas?) {
        canvas?.drawText(value.toString(), left, top, paint);
    }

}