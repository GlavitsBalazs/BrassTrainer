package hu.glavits.brasstrainer.typesetting

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import hu.glavits.brasstrainer.R
import hu.glavits.brasstrainer.R.dimen.*
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.abs
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

    //val symbols = listOf(
    //    GClef(),
    //    Sharp(4),
    //    Sharp(1),
    //    Sharp(5),
    //    Sharp(2),
    //    Sharp(-1),
    //    Sharp(3),
    //    Sharp(0),
    //    QuarterNote(-3)
    //)

   //val symbols = listOf(
   //    GClef(),
   //    Flat(0),
   //    Flat(3),
   //    Flat(-1),
   //    Flat(2),
   //    Flat(-2),
   //    Flat(1),
   //    Flat(-3),
   //    QuarterNote(-3)
   //)

    val symbols = listOf(
        GClef(),
        Flat(-3),
        QuarterNote(-3)
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        staff.drawLines(canvas)
        var x = scale * resources.getDimension(staff_beginning_margin)
        for (s in symbols) {
            x += scale * resources.getDimension(s.leftMargin)
            if (s is QuarterNote) {
                staff.drawLedgerLines(canvas, s.notePosition, x)
            }
            val drawable = ResourcesCompat.getDrawable(resources, s.drawable, null)
            drawable?.let {
                val vo = scale * resources.getDimension(s.verticalOffset)
                val y = staff.notePosition(s.notePosition) + vo
                it.setBounds(
                    x.toInt(), y.toInt(),
                    (x + scale * drawable.intrinsicWidth).toInt(),
                    (y + scale * drawable.intrinsicHeight).toInt()
                )
                it.draw(canvas)
                x += scale * drawable.intrinsicWidth
            }
            x += scale * resources.getDimension(s.rightMargin)
        }
    }

    inner class Staff(scale: Float, private val zeroPosition: Float) {
        private val lineCount = 5
        private val paint = Paint()
        private val spacing = scale * resources.getDimension(staff_spacing)
        private val ledgerLeft =
            scale * (resources.getDimension(staff_ledger_line_width) - resources.getDimension(symbol_note_width)) / 2.0F
        private val ledgerRight =
            scale * (resources.getDimension(staff_ledger_line_width) + resources.getDimension(symbol_note_width)) / 2.0F

        init {
            paint.color = Color.BLACK
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = scale * resources.getDimension(staff_stroke_width)
        }

        private fun linePosition(lineIndex: Float): Float {
            return zeroPosition + lineIndex * spacing
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
            var extraLine = (lineCount / 2 + 1)
            if (abs(lineIndex) < extraLine) return
            extraLine *= (if (lineIndex < 0) -1 else 1)
            for (i in min(lineIndex, extraLine)..max(lineIndex, extraLine)) {
                val y = linePosition(i.toFloat())
                canvas.drawLine(x - ledgerLeft, y, x + ledgerRight, y, paint)
            }
        }
    }
}
