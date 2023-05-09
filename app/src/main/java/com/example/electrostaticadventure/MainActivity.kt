package com.example.electrostaticadventure

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.electrostaticadventure.mathmodule.Vector2D
import com.example.electrostaticadventure.mathmodule.Vector2D.Companion.magSquared
import com.example.electrostaticadventure.mathmodule.Vector2D.Companion.normal

class MainActivity : AppCompatActivity() {

    lateinit var gameManager: GameManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameManager = findViewById(R.id.gameView);
        gameManager.setWillNotDraw(true);
        gameManager.invalidate();

    }

    override fun onPause() {
        super.onPause();
        gameManager.pause();
    }

    override fun onResume() {
        super.onResume()
        gameManager.resume();

    }

}