package hu.glavits.brasstrainer.typesetting

import hu.glavits.brasstrainer.model.Accidental
import hu.glavits.brasstrainer.model.Clef
import hu.glavits.brasstrainer.model.KeySignature
import hu.glavits.brasstrainer.model.Note
import kotlin.math.abs

/**
 * This class represents the information required to uniquely specify a pitch in musical notation.
 */
class StaffConfiguration(
    val clef: Clef,
    val keySignature: KeySignature,
    val accidental: Accidental?,
    val note: Note
) {
    constructor(clef: Clef, note: Note)
            : this(clef, KeySignature.C_MAJOR, null, note)

    constructor(clef: Clef, keySignature: KeySignature, note: Note)
            : this(clef, keySignature, null, note)

    /**
     * The symbols that notate this representation.
     */
    fun produceSymbols(): List<Symbol> {
        val result = ArrayList<Symbol>()
        result.add(clef.symbol)
        result.addAll(getKeySignatureSymbols())
        val noteIndex = note - clef.middleNote
        accidental?.let {
            result.add(getAccidentalSymbol(it, noteIndex))
        }
        result.add(QuarterNote(noteIndex))
        return result
    }

    private fun getKeySignatureSymbols() = sequence {
        val notes = if (keySignature.accidentals > 0)
            KeySignature.SHARP_NOTES else KeySignature.FLAT_NOTES
        val octaves = if (keySignature.accidentals > 0) clef.sharpOctaves else clef.flatOctaves
        for (i in 0 until abs(keySignature.accidentals)) {
            val sigNote = Note(notes[i], octaves[i])
            val noteIndex = sigNote - clef.middleNote
            yield(
                if (keySignature.accidentals > 0) SharpSignature(noteIndex)
                else FlatSignature(noteIndex)
            )
        }
    }

    private fun getAccidentalSymbol(accidental: Accidental, noteIndex: Int) = when (accidental) {
        Accidental.DOUBLE_FLAT -> DoubleFlatAccidental(noteIndex)
        Accidental.FLAT -> FlatAccidental(noteIndex)
        Accidental.NATURAL -> NaturalAccidental(noteIndex)
        Accidental.SHARP -> SharpAccidental(noteIndex)
        Accidental.DOUBLE_SHARP -> DoubleSharpAccidental(noteIndex)
    }
}
