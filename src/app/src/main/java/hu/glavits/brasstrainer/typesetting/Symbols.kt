package hu.glavits.brasstrainer.typesetting

import hu.glavits.brasstrainer.R

/**
 * A drawable that appears on the staff in a specific note index.
 * @see StaffView.Staff.notePosition
 */
abstract class Symbol(val noteIndex: Int) {
    abstract val drawable: Int
    abstract val verticalOffset: Int
    abstract val rightMargin: Int
    open val leftMargin: Int
        get() = R.dimen.dimen_zero
}

class FClef(noteIndex: Int = 2) : Symbol(noteIndex) {
    override val drawable: Int = R.drawable.symbol_f_clef
    override val verticalOffset: Int = R.dimen.symbol_f_clef_vertical_offset
    override val rightMargin: Int = R.dimen.symbol_f_clef_margin
}

class GClef(noteIndex: Int = -2) : Symbol(noteIndex) {
    override val drawable: Int = R.drawable.symbol_g_clef
    override val verticalOffset: Int = R.dimen.symbol_g_clef_vertical_offset
    override val rightMargin: Int = R.dimen.symbol_g_clef_margin
}

class CClef(noteIndex: Int) : Symbol(noteIndex) {
    override val drawable: Int = R.drawable.symbol_c_clef
    override val verticalOffset: Int = R.dimen.symbol_c_clef_vertical_offset
    override val rightMargin: Int = R.dimen.symbol_c_clef_margin
}

class SharpSignature(noteIndex: Int) : Symbol(noteIndex) {
    override val drawable: Int = R.drawable.symbol_sharp
    override val verticalOffset: Int = R.dimen.symbol_sharp_vertical_offset
    override val rightMargin: Int = R.dimen.symbol_sharp_signature_margin
}

class FlatSignature(noteIndex: Int) : Symbol(noteIndex) {
    override val drawable: Int = R.drawable.symbol_flat
    override val verticalOffset: Int = R.dimen.symbol_flat_vertical_offset
    override val rightMargin: Int = R.dimen.symbol_flat_signature_margin
}

class SharpAccidental(noteIndex: Int) : Symbol(noteIndex) {
    override val drawable: Int = R.drawable.symbol_sharp
    override val verticalOffset: Int = R.dimen.symbol_sharp_vertical_offset
    override val rightMargin: Int = R.dimen.symbol_sharp_accidental_margin
    override val leftMargin: Int
        get() = R.dimen.symbol_sharp_accidental_left_margin
}

class FlatAccidental(noteIndex: Int) : Symbol(noteIndex) {
    override val drawable: Int = R.drawable.symbol_flat
    override val verticalOffset: Int = R.dimen.symbol_flat_vertical_offset
    override val rightMargin: Int = R.dimen.symbol_flat_accidental_margin
    override val leftMargin: Int
        get() = R.dimen.symbol_flat_accidental_left_margin
}

class NaturalAccidental(noteIndex: Int) : Symbol(noteIndex) {
    override val drawable: Int = R.drawable.symbol_natural
    override val verticalOffset: Int = R.dimen.symbol_natural_vertical_offset
    override val rightMargin: Int = R.dimen.symbol_natural_margin
    override val leftMargin: Int
        get() = R.dimen.symbol_natural_left_margin
}

class DoubleSharpAccidental(noteIndex: Int) : Symbol(noteIndex) {
    override val drawable: Int = R.drawable.symbol_double_sharp
    override val verticalOffset: Int = R.dimen.symbol_double_sharp_vertical_offset
    override val rightMargin: Int = R.dimen.symbol_double_sharp_margin
    override val leftMargin: Int
        get() = R.dimen.symbol_double_sharp_left_margin
}

class DoubleFlatAccidental(noteIndex: Int) : Symbol(noteIndex) {
    override val drawable: Int = R.drawable.symbol_double_flat
    override val verticalOffset: Int = R.dimen.symbol_double_flat_vertical_offset
    override val rightMargin: Int = R.dimen.symbol_double_flat_margin
    override val leftMargin: Int
        get() = R.dimen.symbol_double_flat_left_margin
}

class QuarterNote(noteIndex: Int) : Symbol(noteIndex) {
    override val drawable: Int =
        if (noteIndex > 0) R.drawable.symbol_downwards_quarter_note
        else R.drawable.symbol_upwards_quarter_note
    override val verticalOffset: Int =
        if (noteIndex > 0) R.dimen.symbol_downwards_quarter_note_vertical_offset
        else R.dimen.symbol_upwards_quarter_note_vertical_offset
    override val rightMargin: Int = R.dimen.symbol_note_right_margin
    override val leftMargin: Int
        get() = R.dimen.symbol_note_left_margin
}