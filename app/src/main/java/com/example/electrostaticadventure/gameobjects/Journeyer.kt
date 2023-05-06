package com.example.electrostaticadventure.gameobjects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.example.electrostaticadventure.R
import com.example.electrostaticadventure.gameobjects.map.Block
import com.example.electrostaticadventure.gameobjects.map.Wall
import com.example.electrostaticadventure.gameobjects.map.WallBlock
import com.example.electrostaticadventure.gameobjects.plaques.Plaque
import com.example.electrostaticadventure.mathmodule.Vector2D
import com.example.electrostaticadventure.mathmodule.Vector2D.Companion.mag

class Journeyer(
    private val field: Field, var position: Vector2D, private val radius: Float, maxSpeed: Float,
    private val walls: ArrayList<Wall>, private val blocks: ArrayList<Block>/*private val map : Labyrinth,*/, private val plaques: ArrayList<Plaque>,
    private val finishing: Plaque, context: Context
) {

    private var polarity = 1f
    private var speed = Vector2D(0f, 0f);
    private val maxSpeedSquared = maxSpeed * maxSpeed;

    private val bodyPosTexture = BitmapFactory.decodeResource(context.resources, R.drawable.pjcs1);
    private val bodyNegTexture = BitmapFactory.decodeResource(context.resources, R.drawable.pjcs1);
    private var bodyTexture = bodyPosTexture;

    private val eyesUpLeft = BitmapFactory.decodeResource(context.resources, R.drawable.eyes_upleft);
    private val eyesUpRight = BitmapFactory.decodeResource(context.resources, R.drawable.eyes_upright);
    private val eyesDownLeft = BitmapFactory.decodeResource(context.resources, R.drawable.eyes_downleft);
    private val eyesDownRight = BitmapFactory.decodeResource(context.resources, R.drawable.eyes_downright);
    private lateinit var eyes: Bitmap;

    private var moustacheTexture = BitmapFactory.decodeResource(context.resources, R.drawable.moustache);


    var hitBox = RectF(
        position.x - radius, position.y - radius,
        position.x + radius, position.y + radius
    );



    private val paint = Paint();

    init {
        paint.color = Color.YELLOW;

    }

    // dt determined by the real time passed
    fun update(dt: Float) {
        val acc = field.getFieldAt(position).times(polarity)

        if (mag(speed + acc * dt) < maxSpeedSquared) speed += acc * dt;
        val dl = speed * dt;
        position += dl;

        updateHitBox();

        for (wall in walls) wall.check(this, dt);

        //map.check(this,  dt)
        for (block in blocks) block.check(this, dt)

        for (plaque in plaques) {
            plaque.check(this);
        }
        finishing.check(this);
    }

    private fun updateHitBox() {
        hitBox.offsetTo(position.x - radius, position.y - radius);
    }

    // orientation: true means horizontal wall
    fun bounce(orientation: Boolean, dt: Float) {
        if (orientation) speed.y *= -1;
        else speed.x *= -1;
        val dl = speed * (2 * dt);
        position += dl;
        updateHitBox();
    }

    fun polarityChange() {
        polarity *= -1
    }

    fun draw(canvas: Canvas?) {
        eyes = if (speed.x > 0) {
            if (speed.y > 0) eyesDownRight;
            else eyesUpRight;
        } else {
            if (speed.y > 0) eyesDownLeft;
            else eyesUpLeft;
        }
        canvas?.drawBitmap(bodyTexture, null, hitBox, null);
        canvas?.drawBitmap(eyes, null, hitBox, null);
        canvas?.drawBitmap(moustacheTexture, null, hitBox, null);
    }
}