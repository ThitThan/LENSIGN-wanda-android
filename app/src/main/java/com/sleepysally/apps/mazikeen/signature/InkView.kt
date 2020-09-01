package com.sleepysally.apps.mazikeen.signature

import android.app.AlertDialog
import android.content.Context
import android.graphics.*
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.sleepysally.apps.mazikeen.signature.model.InkPoint
import kotlin.math.abs


/**
 *  Based on
 *  https://medium.com/@ssaurel/learn-to-create-a-paint-application-for-android-5b16968063f8
 */

class InkView : View {
    private val TOUCH_TOLERANCE = 0f

    private var mX: Float = 0.toFloat()
    private var mY: Float = 0.toFloat()
    private var mPath: Path? = null
    private var mPaint: Paint = Paint()
    private var paths: ArrayList<InkPath> = ArrayList()

    private var currentColor: Int = Color.DKGRAY
    private var bgColor = Color.WHITE
    private var strokeWidth: Int = 3

    private val supportedInputTools = arrayListOf(
        MotionEvent.TOOL_TYPE_STYLUS,
        MotionEvent.TOOL_TYPE_FINGER
    )

//    private var emboss: Boolean = false
//    private var blur: Boolean = false
//    private var mEmboss: MaskFilter? = null
    private var mBlur: MaskFilter? = null

    private lateinit var mBitmap: Bitmap
    private lateinit var mCanvas: Canvas
    private var mBitmapPaint = Paint(Paint.DITHER_FLAG)

    // DATA
    private var inkPoints: ArrayList<InkPoint> = ArrayList()

    // EVENT LISTENERS
    lateinit var onSigningFinished: (ArrayList<InkPoint>) -> Boolean

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.color = currentColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.xfermode = null
        mPaint.alpha = 0xff

//        mEmboss = EmbossMaskFilter(floatArrayOf(1f,1f,1f), 0.4f, 6f, 3.5f)
        mBlur = BlurMaskFilter(5f, BlurMaskFilter.Blur.NORMAL)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        // init bitmap
        var dm = DisplayMetrics()
        display.getMetrics(dm)
        mBitmap = Bitmap.createBitmap(dm.widthPixels, dm.heightPixels, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)

        this.setupCountDown()
    }

    fun clear() {
//        this.bgColor = DEFAULT_BG_COLOR
        this.paths.clear()
        this.inkPoints.clear()
        this.mCountDownTimer?.cancel()

        this.invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        mCanvas?.drawColor(bgColor)

        for (fp in paths) {
            mPaint.color = fp.color
            mPaint.strokeWidth = fp.strokeWidth.toFloat()
            mPaint.maskFilter = null

//            if (fp.emboss)
//                mPaint.maskFilter = mEmboss
            if (fp.blur)
                mPaint.maskFilter = mBlur

            mCanvas?.drawPath(fp.path!!, mPaint)

        }

        canvas.drawBitmap(mBitmap, 0f, 0f, mBitmapPaint)
        canvas.restore()
    }

    //
    // Touch
    //
    private fun touchStart(x: Float, y: Float) {
        mPath = Path()
        val fp = InkPath(currentColor, this.strokeWidth, false, false, mPath!!)
        paths.add(fp)

        mPath?.reset()
        mPath?.moveTo(x, y)
        mX = x
        mY = y

        this.mCountDownTimer?.cancel()
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = abs(x - mX)
        val dy = abs(y - mY)

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath?.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    private fun touchUp() {
        mPath?.lineTo(mX, mY)
        this.mCountDownTimer?.start()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y


        if (this.supportedInputTools.contains(event.getToolType(0))) {
            // Record Data
            Log.d("pen", "${x} ${y} ${event.pressure}")
            this.inkPoints.add(InkPoint(x, y, event.pressure, System.currentTimeMillis()))

            // Draw
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    touchStart(x, y)
                    invalidate()
                }
                MotionEvent.ACTION_MOVE -> {
                    touchMove(x, y)
                    invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    touchUp()
                    invalidate()
                }
            }
        }

        return true
    }

    var mCountDownTimer: CountDownTimer? = null
    fun setupCountDown() {
        mCountDownTimer = object : CountDownTimer(1000, 200) {
            override fun onTick(millisUntilFinished: Long) {
                // do nothing..
            }

            override fun onFinish() {
                val shouldClearStrokes = onSigningFinished(inkPoints);
                if (shouldClearStrokes) {
                    clear()
                }
            }

        }

    }
}