package com.suryashakti.solarmonitor.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progress = 0f
    private var label = "Solar"
    private var subLabel = ""

    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#2D2D2D")
        style = Paint.Style.STROKE
        strokeWidth = dpToPx(18f)
        strokeCap = Paint.Cap.ROUND
    }
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = dpToPx(18f)
        strokeCap = Paint.Cap.ROUND
    }
    private val centerTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    private val subTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#BDBDBD")
        textAlign = Paint.Align.CENTER
    }
    private val subLabelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FFD600")
        textAlign = Paint.Align.CENTER
    }

    private val arcRect = RectF()

    fun setProgress(value: Float, labelText: String = "Solar", sub: String = "") {
        progress = value.coerceIn(0f, 100f)
        label = labelText
        subLabel = sub
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val stroke = dpToPx(18f)
        val pad = stroke + dpToPx(8f)
        arcRect.set(pad, pad, width - pad, height - pad)

        canvas.drawArc(arcRect, 135f, 270f, false, trackPaint)

        val sweep = (progress / 100f) * 270f
        progressPaint.shader = SweepGradient(
            width / 2f, height / 2f,
            intArrayOf(Color.parseColor("#FF8F00"), Color.parseColor("#FFD600")),
            floatArrayOf(0f, 1f)
        )
        canvas.save()
        canvas.rotate(135f, width / 2f, height / 2f)
        canvas.drawArc(arcRect, 0f, sweep, false, progressPaint)
        canvas.restore()

        val cx = width / 2f
        val cy = height / 2f

        centerTextPaint.textSize = dpToPx(28f)
        canvas.drawText("${progress.toInt()}%", cx, cy - dpToPx(4f), centerTextPaint)

        subTextPaint.textSize = dpToPx(11f)
        canvas.drawText(label, cx, cy + dpToPx(14f), subTextPaint)

        if (subLabel.isNotEmpty()) {
            subLabelPaint.textSize = dpToPx(10f)
            canvas.drawText(subLabel, cx, cy + dpToPx(28f), subLabelPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = MeasureSpec.getSize(widthMeasureSpec)
            .coerceAtMost(MeasureSpec.getSize(heightMeasureSpec))
            .coerceAtLeast(dpToPx(140f).toInt())
        setMeasuredDimension(size, size)
    }

    private fun dpToPx(dp: Float): Float =
        dp * context.resources.displayMetrics.density
}
