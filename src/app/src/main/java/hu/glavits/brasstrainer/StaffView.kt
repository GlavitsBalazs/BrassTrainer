package hu.glavits.brasstrainer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import hu.glavits.brasstrainer.R.dimen.*
import kotlin.math.truncate

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

    private var _scale: Float = 1f
    var scale: Float
        get() = _scale
        set(value) {
            _scale = value
            invalidateStaff()
        }

    private fun invalidateStaff() {
        staff = Staff(_scale, height / 2.0F)
    }

    private lateinit var staff: Staff

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.StaffView, defStyle, 0
        )
        _scale = a.getFloat(R.styleable.StaffView_scale, scale)
        a.recycle()
        invalidateStaff()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        invalidateStaff()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        staff.drawLines(canvas)
        staff.drawLedgerLines(canvas, 8, 280.0F)
        drawSymbol(FClef(2), canvas, 10)
    }

    fun drawSymbol(s: Symbol, canvas: Canvas, x: Int) {
        val drawable: Drawable? = ResourcesCompat.getDrawable(resources, s.drawable, null)
        drawable?.let {
            val vo = scale * resources.getDimension(s.verticalOffset)
            val y = (staff.notePosition(s.notePosition) + vo).toInt()
            it.setBounds(
                x, y,
                x + (scale * drawable.intrinsicWidth).toInt(),
                y + (scale * drawable.intrinsicHeight).toInt()
            )
            it.draw(canvas)
        }
    }

    abstract class Symbol(val notePosition: Int) {
        abstract val drawable: Int
        abstract val verticalOffset: Int
        abstract val leftMargin: Int
        abstract val rightMargin: Int
    }

    class FClef(notePosition: Int) : Symbol(notePosition) {
        override val drawable: Int = R.drawable.ic_f_clef
        override val verticalOffset: Int = f_clef_vertical_offset
        override val leftMargin: Int = dimen_zero
        override val rightMargin: Int = dimen_zero
    }

    inner class Staff(scale: Float, private val zeroPosition: Float) {
        private val lineCount = 5
        private val paint = Paint()
        private val spacing = scale * resources.getDimension(staff_spacing)
        private val ledger = scale * resources.getDimension(ledger_line_width)

        init {
            paint.color = Color.BLACK
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = scale * resources.getDimension(staff_stroke_width)
        }

        private fun linePosition(lineIndex: Float): Float {
            return zeroPosition + lineIndex * (spacing)  //+ paint.strokeWidth)
        }

        fun notePosition(noteIndex: Int): Float {
            return linePosition(-noteIndex / 2.0F)
        }

        fun drawLines(canvas: Canvas) {
            for (i in -lineCount / 2..lineCount / 2) {
                val y = linePosition(i.toFloat())
                canvas.drawLine(0F, y, canvas.width.toFloat(), y, paint)
            }
        }

        fun drawLedgerLines(canvas: Canvas, noteIndex: Int, x: Float) {
            val lineIndex = truncate(-noteIndex / 2.0F).toInt()
            for (i in lineIndex until (if (lineIndex < 0) -1 else 1) * lineCount / 2) {
                val y = linePosition(i.toFloat())
                canvas.drawLine(x - ledger / 2.0F, y, x + ledger / 2.0F, y, paint)
            }
        }
    }
}
