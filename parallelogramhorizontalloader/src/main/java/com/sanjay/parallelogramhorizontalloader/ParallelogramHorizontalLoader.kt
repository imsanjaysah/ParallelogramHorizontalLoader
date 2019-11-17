package com.sanjay.parallelogramhorizontalloader

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat

import com.sanjay.parallelogramhorizontalloader.extensions.addToCompositeDisposable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

/**
 * @author sanjay.sah
 *
 * Custom Horizontal loader
 */
class ParallelogramHorizontalLoader : LinearLayout {
    private val path = Path()

    private var mParallogramSize: Float = 0f
    private var mAnimateFillColor: Int = 0
    private var mNormalFillColor: Int = 0
    private var mSelectionIntervalDuration: Int = 0
    private var mParallogramCount: Int = 0

    private var disposeBag: CompositeDisposable = CompositeDisposable()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (isInEditMode)
            return

        if (attrs == null) {
            throw IllegalArgumentException("Attributes should not be null")
        }
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.ParallelogramHorizontalLoader)
        mParallogramSize = typedArray.getDimension(
            R.styleable.ParallelogramHorizontalLoader_pg_size,
            resources.getDimension(R.dimen.parallogram_size)
        )
        mAnimateFillColor = typedArray.getColor(
            R.styleable.ParallelogramHorizontalLoader_pg_animate_fill_color,
            ContextCompat.getColor(context, R.color.red)
        )
        mNormalFillColor = typedArray.getColor(
            R.styleable.ParallelogramHorizontalLoader_pg_normal_fill_color,
            ContextCompat.getColor(context, R.color.white)
        )
        mSelectionIntervalDuration = typedArray.getInt(
            R.styleable.ParallelogramHorizontalLoader_pg_selection_interval_duration,
            DEFAULT_DURATION_TIME
        )
        mParallogramCount =
            typedArray.getInt(R.styleable.ParallelogramHorizontalLoader_pg_count, DEFAULT_COUNT)
        typedArray.recycle()

        gravity = Gravity.CENTER_HORIZONTAL
        orientation = HORIZONTAL


        for (i in 0 until mParallogramCount) {
            val parallogramView = ParallogramView(context, i)
            addView(parallogramView, mParallogramSize.toInt(), mParallogramSize.toInt())
        }
    }

    inner class ParallogramView(context: Context, index: Int) : View(context) {
        private var mInnerPaint: Paint = Paint()

        init {

            mInnerPaint.isAntiAlias = true
            mInnerPaint.color = mNormalFillColor
            mInnerPaint.style = Paint.Style.FILL
            mInnerPaint.strokeJoin = Paint.Join.ROUND

            Observable
                .interval(
                    ((index + 1) * mSelectionIntervalDuration).toLong(),
                    (mParallogramCount * mSelectionIntervalDuration).toLong(),
                    TimeUnit.MILLISECONDS
                )
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mInnerPaint.color = mAnimateFillColor
                    postInvalidate()
                    Observable
                        .interval(mSelectionIntervalDuration.toLong(), TimeUnit.MILLISECONDS)
                        .take(1)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            mInnerPaint.color = mNormalFillColor
                            postInvalidate()
                        }.addToCompositeDisposable(disposeBag)
                }.addToCompositeDisposable(disposeBag)
        }

        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)
            val width = mParallogramSize
            val height = mParallogramSize
            path.moveTo(width, 0f)
            path.lineTo((width / 4), 0f)
            path.lineTo(0f, height)
            path.lineTo(((width / 4) * 3), height)
            path.lineTo(width, 0f)
            canvas?.drawPath(path, mInnerPaint)
        }


    }

    /**
     * Stop horizontal loading animation
     */
    fun stopLoading() {
        disposeBag.clear()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposeBag.clear()
    }

    companion object {
        private const val DEFAULT_DURATION_TIME = 300
        private const val DEFAULT_COUNT = 6
    }
}