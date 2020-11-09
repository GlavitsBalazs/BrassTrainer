package hu.glavits.brasstrainer.model

import hu.glavits.brasstrainer.typesetting.*
import kotlin.math.abs

/**
 * @param symbol The clef symbol.
 * @param middleNote The note of the middle line, as set by the clef.
 * @param sharpOctaves The octaves of the key signature sharps.
 * @param flatOctaves The octaves of the key signature flats.
 */
enum class Clef(
    val symbol: Symbol, val middleNote: Note,
    val sharpOctaves: List<Int>, val flatOctaves: List<Int>
) {
    TREBLE_CLEF(
        GClef(-2), Note(NoteName.B, 4),
        listOf(5, 5, 5, 5, 4, 5, 4), listOf(4, 5, 4, 5, 4, 5, 4)
    ),
    BASS_CLEF(
        FClef(2), Note(NoteName.D, 3),
        listOf(3, 3, 3, 3, 2, 3, 2), listOf(2, 3, 2, 3, 2, 3, 2)
    ),
}

/**
 * @param accidentals The number of accidentals.
 *  Negative accidentals mean flats, positive means sharps.
 */
enum class KeySignature(val accidentals: Int) {
    C_FLAT_MAJOR(-7),
    G_FLAT_MAJOR(-6),
    D_FLAT_MAJOR(-5),
    A_FLAT_MAJOR(-4),
    E_FLAT_MAJOR(-3),
    B_FLAT_MAJOR(-2),
    F_MAJOR(-1),
    C_MAJOR(0),
    G_MAJOR(1),
    D_MAJOR(2),
    A_MAJOR(3),
    E_MAJOR(4),
    B_MAJOR(5),
    F_SHARP_MAJOR(6),
    C_SHARP_MAJOR(7);

    fun getAccidental(note: Note): Accidental {
        val notes = if (accidentals > 0) SHARP_NOTES else FLAT_NOTES
        if (notes.subList(0, abs(accidentals)).contains(note.name))
            return if (accidentals > 0) Accidental.SHARP else Accidental.FLAT
        return Accidental.NATURAL
    }

    companion object {
        val SHARP_NOTES = listOf(
            NoteName.F,
            NoteName.C,
            NoteName.G,
            NoteName.D,
            NoteName.A,
            NoteName.E,
            NoteName.B
        )

        val FLAT_NOTES = listOf(
            NoteName.B,
            NoteName.E,
            NoteName.A,
            NoteName.D,
            NoteName.G,
            NoteName.C,
            NoteName.F
        )
    }
}

class StaffConfiguration(
    val clef: Clef,
    val keySignature: KeySignature,
    val accidental: Accidental?,
    val note: Note
) {

    fun produceSymbols(): List<Symbol> {
        val result = ArrayList<Symbol>()
        result.add(clef.symbol)
        val notes = if (keySignature.accidentals > 0)
            KeySignature.SHARP_NOTES else KeySignature.FLAT_NOTES
        val octaves = if (keySignature.accidentals > 0) clef.sharpOctaves else clef.flatOctaves
        for (i in 0 until abs(keySignature.accidentals)) {
            val sigNote = Note(notes[i], octaves[i])
            val pos = sigNote - clef.middleNote
            result.add(
                if (keySignature.accidentals > 0) SharpSignature(
                    pos
                ) else FlatSignature(pos)
            )
        }
        val notePosition = note - clef.middleNote
        accidental?.let {
            result.add(
                when (it) {
                    Accidental.FLAT -> FlatAccidental(
                        notePosition
                    )
                    Accidental.NATURAL -> TODO()
                    Accidental.SHARP -> SharpAccidental(
                        notePosition
                    )
                }
            )
        }
        result.add(QuarterNote(note - clef.middleNote))
        return result
    }
}
