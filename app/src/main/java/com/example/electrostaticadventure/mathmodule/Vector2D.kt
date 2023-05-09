package com.example.electrostaticadventure.mathmodule

import kotlin.math.sqrt

data class Vector2D(var x: Float, var y: Float) {

    companion object {
        fun mag(vector2D: Vector2D): Float {
            return vector2D.mag();
        }

        fun magSquared(vector2D: Vector2D): Float {
            return vector2D.magSquared();
        }

        fun normal(vector2D: Vector2D): Vector2D {
            return vector2D.normal();
        }
    }

    operator fun plus(other: Vector2D): Vector2D {
        return Vector2D(x + other.x, y + other.y);
    }


    operator fun unaryMinus(): Vector2D {
        return Vector2D(-x, -y);
    }

    operator fun minus(other: Vector2D): Vector2D {
        return this + (-other);
    }

    operator fun times(scalar: Float): Vector2D {
        return Vector2D(x * scalar, y * scalar);
    }


    operator fun div(scalar: Float): Vector2D {
        return this * (1 / scalar);
    }

    operator fun times(other: Vector2D): Float {
        return x * other.x + y * other.y;
    }

    private fun magSquared(): Float {
        return x * x + y * y;
    }

    private fun mag(): Float {
        return sqrt(magSquared());
    }

    private fun normal(): Vector2D {
        return (this / this.mag());
    }

    override fun toString(): String {
        return "[$x, $y]";
    }

    fun xVector(): Vector2D {
        return Vector2D(x, 0f);
    }

    fun yVector(): Vector2D {
        return Vector2D(0f, y);
    }

}

operator fun Float.times(vector2D: Vector2D): Vector2D {
    return vector2D * this;
}