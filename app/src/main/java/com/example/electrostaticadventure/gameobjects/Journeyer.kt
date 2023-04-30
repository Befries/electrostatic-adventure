package com.example.electrostaticadventure.gameobjects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.example.electrostaticadventure.mathmodule.Vector2D
import java.util.Random

class Journeyer(
    private val field: Field, var position: Vector2D, diameter: Float,
    private val walls: ArrayList<Wall>, private val plaques: ArrayList<Plaque>,
    private val finishing: Plaque
) {

    private var polarity = 1
    private var speed = Vector2D(0f, 0f);

    var hitBox = RectF(
        position.x - diameter / 2, position.y - diameter / 2,
        position.x + diameter / 2, position.y + diameter / 2
    );

    private val paint = Paint();
    private val random = Random();
    var color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));


    // dt determined by the real time passed
    fun update(dt: Float) {
        val acc = field.getFieldAt(position);
        speed += acc * dt;
        val dl = speed * dt;

        hitBox.offset(dl.x, dl.y);

        for (wall in walls) {
            wall.check(this);
            /*wall check if the charge touches the wall.
            in this case the wall calls the function bounce*/
        }

        for (plaque in plaques) {
            plaque.check(this);
        }
        finishing.check(this);
    }

    // orientation: true means horizontal wall
    fun bounce(orientation: Boolean) {
        if (orientation) speed.y *= -1;
        else speed.x *= -1;
    }

    fun polarityChange() {
        polarity *= -1
    }

    fun draw(canvas: Canvas?) {
        paint.color = color
        canvas?.drawOval(hitBox, paint)
    }
}