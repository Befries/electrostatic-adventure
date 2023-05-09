package com.example.electrostaticadventure.uiobjects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.electrostaticadventure.Drawer
import kotlin.math.round

class GameCounter(private val left: Float, private val top: Float, var value: Float) : Drawer {

    public val paint = Paint();
    public var rounded = true;

    init {
        paint.color = Color.BLACK;
        paint.textSize = 200f;
    }


    override fun draw(canvas: Canvas?) {
        if (rounded) canvas?.drawText(value.toInt().toString(), left, top, paint);
        else canvas?.drawText((round(value*100)/100).toString(), left, top, paint);
    }

}