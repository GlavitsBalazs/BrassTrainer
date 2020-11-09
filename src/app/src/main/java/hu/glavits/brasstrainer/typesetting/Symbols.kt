package hu.glavits.brasstrainer.typesetting

import hu.glavits.brasstrainer.R

abstract class Symbol(val notePosition: Int) {
    abstract val drawable: Int
    abstract val verticalOffset: Int
    abstract val rightMargin: Int
    open val leftMargin: Int
        get() = R.dimen.dimen_zero
}

class FClef(notePosition: Int = 2) : Symbol(notePosition) {
    override val drawable: Int = R.drawable.symbol_f_clef
    override val verticalOffset: Int = R.dimen.symbol_f_clef_vertical_offset
    override val rightMargin: Int = R.dimen.symbol_f_clef_margin
}

class GClef(notePosition: Int = -2) : Symbol(notePosition) {
    override val drawable: Int = R.drawable.symbol_g_clef
    override val verticalOffset: Int = R.dimen.symbol_g_clef_vertical_offset
    override val rightMargin: Int = R.dimen.symbol_g_clef_margin
}

class SharpSignature(notePosition: Int) : Symbol(notePosition) {
    override val drawable: Int = R.drawable.symbol_sharp
    override val verticalOffset: Int = R.dimen.symbol_sharp_vertical_offset
    override val rightMargin: Int = R.dimen.symbol_sharp_signature_margin
}

class FlatSignature(notePosition: Int) : Symbol(notePosition) {
    override val drawable: Int = R.drawable.symbol_flat
    override val verticalOffset: Int = R.dimen.symbol_flat_vertical_offset
    override val rightMargin: Int = R.dimen.symbol_flat_accidental_margin
}

class SharpAccidental(notePosition: Int) : Symbol(notePosition) {
    override val drawable: Int = R.drawable.symbol_sharp
    override val verticalOffset: Int = R.dimen.symbol_sharp_vertical_offset
    override val rightMargin: Int = R.dimen.symbol_sharp_signature_margin
}

class FlatAccidental(notePosition: Int) : Symbol(notePosition) {
    override val drawable: Int = R.drawable.symbol_flat
    override val verticalOffset: Int = R.dimen.symbol_flat_vertical_offset
    override val rightMargin: Int = R.dimen.symbol_flat_accidental_margin
}

class QuarterNote(notePosition: Int) : Symbol(notePosition) {
    override val drawable: Int =
        if (notePosition > 0) R.drawable.symbol_downwards_quarter_note
        else R.drawable.ic_upwards_quarter_note
    override val verticalOffset: Int =
        if (notePosition > 0) R.dimen.symbol_downwards_quarter_note_vertical_offset
        else R.dimen.symbol_upwards_quarter_note_vertical_offset
    override val rightMargin: Int = R.dimen.symbol_note_right_margin
    override val leftMargin: Int
        get() = R.dimen.symbol_note_left_margin
}