package com.example.doodle

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.doodle.customview.DoodleView
import com.example.doodle.customview.ImageLoader

class MainActivity : AppCompatActivity() {
    var doodleView: DoodleView? = null
    var backgroundImageView : ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        doodleView = findViewById(R.id.doodleView)
        backgroundImageView = findViewById(R.id.iv_rawImage)
        ImageLoader.loadImage(
            this.applicationContext,
            backgroundImageView!!
        )
    }

    fun onRubberClick(view: View) {
        doodleView?.enableRubber()
    }

    fun onSaveBitmap(view: View) {

    }

}