package com.example.doodle

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.doodle.customview.DoodleView

class MainActivity: AppCompatActivity() {
    var doodleView: DoodleView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
         doodleView = findViewById(R.id.doodleView)
    }


    fun onRubberClick(view: View) {
        doodleView?.enableRubber()
    }
}