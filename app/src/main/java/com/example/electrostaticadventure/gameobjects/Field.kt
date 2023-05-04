package com.example.electrostaticadventure.gameobjects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import com.example.electrostaticadventure.Drawer
import com.example.electrostaticadventure.GameManager
import com.example.electrostaticadventure.mathmodule.Vector2D
import com.example.electrostaticadventure.mathmodule.Vector2D.Companion.mag
import com.example.electrostaticadventure.mathmodule.Vector2D.Companion.normal

class Field(var rows: Int, var columns: Int): ArrayList<Charge>(), Drawer {

    var paint = Paint();
    private var cells = ArrayList<Cell>();

    init {
        // define field lines colors
        paint.color = Color.argb(140, 66, 170, 255);
        paint.strokeWidth = 3f;
    }

    inner class Cell(val bodyCenter: Vector2D, var value: Vector2D) {
        // class of the arrows showing up on the field
        private var back = bodyCenter - value/2f;
        private var front = back + value;

        fun changeValue(newValue: Vector2D) {
            value = newValue;
            back = bodyCenter - value/2f;
            front = back + value;
        }
        fun draw(canvas: Canvas?) {
            canvas?.drawLine(back.x, back.y, front.x, front.y, paint);
        }
    }

    fun generateCellArray(w: Int, h: Int) {
        // initiate Array of cells
        val cellSizeX = w.toFloat() / columns.toFloat();
        val cellSizeY = h.toFloat() / rows.toFloat();
        for (i in 0..columns) {
            for (j in 0..rows) {
                // middle vector
                val pos = Vector2D((cellSizeX * (i.toFloat() + 1/2)), (cellSizeY * (j.toFloat() + 1/2)));
                // instantiate cell; access a particular cell with index = (x + y * rows)
                cells.add(Cell(pos, getFieldAt(pos)));
            }
        }
    }

    private fun updateCells() {
        // recalculate the field vector in each cell
        for (i in 0..columns) {
            for (j in 0..rows) {
                val current = cells[j+i*rows];
                current.changeValue(getFieldAt(current.bodyCenter));
            }
        }
    }

    override fun add(element: Charge): Boolean {
        val bool = super.add(element)
        updateCells();
        return bool;
    }


    fun getFieldAt(pos: Vector2D): Vector2D {
        var totalField = Vector2D(0f, 0f);
        for (c in this) totalField += c.getFieldAt(pos);

        return if (mag(totalField) < GameManager.maxTotalField) totalField
        else normal(totalField) * GameManager.maxTotalField;
    }

    override fun draw(canvas: Canvas?) {
        // draw each arrow and the charges on top
        for (cell in cells) cell.draw(canvas);
        for (charge in this) charge.draw(canvas);
    }
}