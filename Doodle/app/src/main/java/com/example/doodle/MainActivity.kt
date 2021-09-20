package com.example.doodle

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.doodle.customview.DoodleView

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        val doodleView = findViewById<DoodleView>(R.id.doodleView)
    }


    fun onSave(view: View) {

    }
}