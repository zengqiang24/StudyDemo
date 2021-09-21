package com.example.doodle.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.sqrt

private const val TAG = "DoodleView"
private const val RUBBER_RADIUS = 30f

class DoodleView : View, IDoodle {
    private var rubberPressed: Boolean = false
    private var _rubberX = 0f
    private var _rubberY = 0f
    private val actionMap: LinkedHashMap<Int, Edge> = LinkedHashMap()
    private var seq = 0
    private var prePoint: Point? = null
    private var enableRubber = false
    private var rubberMoveListeners = CopyOnWriteArrayList<OnRubberMoveListener>()
    private var _rubberPaint = Paint().apply {
        this.strokeWidth = 10f
        this.color = Color.GRAY
        this.style = Paint.Style.FILL
        this.isAntiAlias = true
    }
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
        actionMap.remove(seq--)?.let {
            removeRubberMoveListener(it)
            invalidate()
        }
    }

    override fun removeEdge(edgeId: Int) {
        actionMap.remove(edgeId)?.let {
            removeRubberMoveListener(it)
            invalidate()
        }
    }

    override fun enableRubber() {
        enableRubber = !enableRubber
        if (!enableRubber) {
            invalidate()
        }
        Log.d(TAG, "enableRubber() called enableRubber = $enableRubber")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            if (enableRubber) {
                rubberPressed = event.action == ACTION_MOVE || event.action == ACTION_DOWN
                updateRubberLocation(event)
            } else {
                var edge: Edge? = null
                when (event.action) {
                    ACTION_DOWN -> {
                        ++seq
                        if (edge == null) edge = Edge(seq, this)
                        actionMap[seq] = edge
                        addRubberMoveListener(edge)
                    }
                    ACTION_MOVE -> {
                        actionMap[seq]?.addPoint(Point(event.x.toInt(), event.y.toInt()))
                        invalidate()
                    }
                    else -> {
                    }
                }
            }
        }
        return true
    }

    private fun updateRubberLocation(event: MotionEvent) {
        _rubberX = event.x
        _rubberY = event.y
        invalidate()
        notifyRubberMoves(event.x, event.y)
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
            if (enableRubber && rubberPressed) {
                drawCircle(_rubberX, _rubberY, RUBBER_RADIUS, _rubberPaint)
            }
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

private class Edge(var id: Int, var doodleView: IDoodle) : OnRubberMoveListener {
    var points: ArrayList<Point> = ArrayList()

    fun addPoint(point: Point) {
        points.add(point)
    }

    override fun update(x: Int, y: Int) {
        for (point in points) {
            val diffx = point.x - x
            val diffy = point.y - y
            val diff = sqrt((diffx * diffx + diffy * diffy).toDouble())
            if (diff <= RUBBER_RADIUS) { //todo 有时候橡皮擦相交了也没有删除画线
                Log.d(TAG, "橡皮擦和画线相交了   x = $x, y = $y diff = $diff")
                doodleView.removeEdge(id)
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
    fun removeEdge(edgeId: Int)
    fun enableRubber()
}