package hu.glavits.brasstrainer.model

import hu.glavits.brasstrainer.typesetting.CClef
import hu.glavits.brasstrainer.typesetting.FClef
import hu.glavits.brasstrainer.typesetting.GClef
import hu.glavits.brasstrainer.typesetting.Symbol
import kotlin.math.abs

/**
 * @param symbol The clef symbol.
 * @param middleNote The note of the middle line, as set by the clef.
 * @param sharpOctaves The octaves where the key signature sharps are displayed.
 * @param flatOctaves The octaves where the key signature flats are displayed.
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
    TENOR_CLEF(
        CClef(2), Note(NoteName.A, 3),
        listOf(3, 4, 3, 4, 3, 4, 3), listOf(3, 4, 3, 4, 3, 4, 3)
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

    /**
     * Is the note flat or sharp in this key signature?
     */
    fun getAccidental(name: NoteName): Accidental {
        val notes = if (accidentals > 0) SHARP_NOTES else FLAT_NOTES
        if (notes.subList(0, abs(accidentals)).contains(name))
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

/**
 * @param degree Degree in the C major scale.
 * @param offset The number of half steps from C.
 */
enum class NoteName(val degree: Int, val offset: Int) {
    C(0, 0),
    D(1, 2),
    E(2, 4),
    F(3, 5),
    G(4, 7),
    A(5, 9),
    B(6, 11);
}

/**
 * @param offset The number of half steps by which this accidental shifts the pitch.
 */
enum class Accidental(val offset: Int) {
    DOUBLE_FLAT(-2),
    FLAT(-1),
    NATURAL(0),
    SHARP(1),
    DOUBLE_SHARP(2),
}

/**
 * @param octave The octave of this note, according to scientific pitch notation.
 */
class Note(val name: NoteName, val octave: Int) {
    /**
     * Diatonic transposition.
     * @param rhs The number degrees in the major scale to shift by.
     */
    operator fun plus(rhs: Int): Note = Note(
        c_major[(name.degree + rhs) % notes_per_octave],
        octave + (name.degree + rhs) / notes_per_octave
    )

    operator fun minus(rhs: Int): Note = this + (-rhs)

    /**
     * The number of major scale degrees separating the notes.
     */
    operator fun minus(rhs: Note): Int =
        (octave * notes_per_octave + name.degree) - (rhs.octave * notes_per_octave + rhs.name.degree)

    companion object {
        val c_major = NoteName.values()
        const val notes_per_octave = 7
    }
}