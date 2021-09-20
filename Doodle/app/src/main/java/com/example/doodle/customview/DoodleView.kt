package com.example.doodle.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View

private const val TAG = "DoodleView"
class DoodleView : View, IDoodle {
    private var _x = 0f
    private var _y = 0f
    private var _endx = 0f
    private var _endY = 0f
    private val actions: ArrayList<List<Point>> = ArrayList<List<Point>>()
    private val _paint: Paint = Paint().apply {
        this.isAntiAlias = true
        this.color = Color.RED
        this.strokeWidth = 22f
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attributes: AttributeSet) : super(
        context,
        attributes,
    )

    override fun setImage(bitmap: Bitmap) {
    }

    override fun generateCombinedImage(): Bitmap? {
        return null
    }

    override fun changePaintColor(color: Int) {
    }

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)
        Log.d(TAG, "onDrawForeground() called with: canvas = $canvas")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            val currentAction = ArrayList<Point>()
            when (event.action) {
                ACTION_DOWN -> {

                }
                ACTION_MOVE ->{
                    currentAction.add(Point(event.x.toInt(), event.y.toInt()))
                    actions.add(currentAction)
                    invalidate()
                }
                ACTION_UP ->{

                }
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            Log.d(TAG, "onDraw: _x = $_x + _y= $_y")
            for (action in actions) {
                for (point in action) {
                    drawPoint(point.x.toFloat(), point.y.toFloat(), _paint)
                }
            }
//            drawLine(_x,_y,_endx,_endY,_paint);
        }
    }
}

public interface IDoodle {
    fun setImage(bitmap: Bitmap)
    fun generateCombinedImage(): Bitmap?
    fun changePaintColor(color: Int)
}