package hu.glavits.brasstrainer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

class StaffView : View {

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private var _scale: Float = 0f
    var scale: Float
        get() = _scale
        set(value) {
            _scale = value
            //invalidateTextPaintAndMeasurements()
        }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.StaffView, defStyle, 0
        )

        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        _scale = a.getDimension(
            R.styleable.StaffView_scale,
            scale
        )

        a.recycle()

        // Update TextPaint and text measurements from attributes
        //invalidateTextPaintAndMeasurements()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawStaves(canvas)

        // Draw the example drawable on top of the text.
        /* val drawable: Drawable? = null
         drawable?.let {
             it.setBounds(
                 paddingLeft, paddingTop,
                 paddingLeft + contentWidth, paddingTop + contentHeight
             )
             it.draw(canvas)
         }*/
    }

    private object StaffParameters {
        val LINE_COUNT: Int = 5
        val LINE_SPACING: Float = 5.0F
        val LINE_WIDTH: Float = 5.0F
    }

    private fun drawStaves(canvas: Canvas) {
        for (i in 0 until StaffParameters.LINE_COUNT) {
            //canvas.drawLine(0, i, width, i, null)
        }
    }
}
