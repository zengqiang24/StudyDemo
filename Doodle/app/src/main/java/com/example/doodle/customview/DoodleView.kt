package com.example.doodle.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import java.util.concurrent.CopyOnWriteArrayList

private const val TAG = "DoodleView"

class DoodleView : View, IDoodle {
    private var _x = 0f
    private var _y = 0f
    private val actionMap: LinkedHashMap<Int, Edge> = LinkedHashMap()
    private var seq = 0
    var prePoint: Point? = null
    private var enableRubber = false
    private var rubberMoveListeners = CopyOnWriteArrayList<OnRubberMoveListener>()
    private val _paint: Paint = Paint().apply {
        this.isAntiAlias = true
        this.style = Paint.Style.STROKE
        this.color = Color.RED
        this.strokeWidth = 10f
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

    override fun revert() {
        val edgeRemoved = actionMap.remove(seq--)
        edgeRemoved?.let { removeRubberMoveListener(it) }
        invalidate()
    }

    override fun revert(edgeId: Int) {
        val edgeRemoved = actionMap.remove(edgeId)
        edgeRemoved?.let { removeRubberMoveListener(it) }
        invalidate()
    }

    override fun enableRubber() {
        enableRubber = !enableRubber
        Log.d(TAG, "enableRubber() called enableRubber = $enableRubber")
    }

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)
        Log.d(TAG, "onDrawForeground() called with: canvas = $canvas")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {

            if (enableRubber) {
                if (event.action == ACTION_MOVE
                    || event.action == ACTION_DOWN
                ) {
                    notifyRubberMoves(event.x, event.y)
                }
            } else {
                var edge: Edge? = null
                when (event.action) {
                    ACTION_DOWN -> {
                        if (edge == null) {
                            edge = Edge(++seq, this)
                        }
                        actionMap.put(++seq, edge)
                        addRubberMoveListener(edge)

                    }
                    ACTION_MOVE -> {
                        edge = actionMap.get(seq)!!
                        edge.addPoint(Point(event.x.toInt(), event.y.toInt()))
                        invalidate()
                    }
                    else -> {
                    }
                }
            }
        }
        return true
    }

    private fun addRubberMoveListener(edge: Edge) {
        rubberMoveListeners.add(edge)
    }

    private fun removeRubberMoveListener(edge: Edge) {
        rubberMoveListeners.remove(edge)
    }

    private fun notifyRubberMoves(x: Float, y: Float) {
        for (rubberMoveListener in rubberMoveListeners) {
            rubberMoveListener.update(x.toInt(), y.toInt())
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            Log.d(TAG, "onDraw: _x = $_x + _y= $_y")
            for (entry in actionMap.entries) {
                prePoint = null
                for (point in entry.value.points) {
                    if (prePoint == null) {
                        prePoint = point
                        continue
                    }
                    drawLine(
                        prePoint!!.x.toFloat(),
                        prePoint!!.y.toFloat(),
                        point.x.toFloat(),
                        point.y.toFloat(),
                        _paint
                    )
                    prePoint = point
                }
            }
        }
    }
}

class Edge(var id: Int, var view: IDoodle) : OnRubberMoveListener {
    var points: ArrayList<Point> = ArrayList()
    fun addPoint(point: Point) {
        points.add(point)
    }

    override fun update(x: Int, y: Int) {
        for (point in points) {
            if (point.x == x && point.y == y) {
                view.revert(id)
            }
        }
    }

}

interface OnRubberMoveListener {
    fun update(x: Int, y: Int)
}

interface IDoodle {
    fun setImage(bitmap: Bitmap)
    fun generateCombinedImage(): Bitmap?
    fun changePaintColor(color: Int)
    fun revert()
    fun revert(edgeId: Int)
    fun enableRubber()
}