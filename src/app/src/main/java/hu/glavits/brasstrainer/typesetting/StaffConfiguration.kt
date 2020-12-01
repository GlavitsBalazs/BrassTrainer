package hu.glavits.brasstrainer.typesetting

import hu.glavits.brasstrainer.model.*

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
        GClef(-2), Note(NoteName.B, Octaves(4)),
        listOf(5, 5, 5, 5, 4, 5, 4), listOf(4, 5, 4, 5, 4, 5, 4)
    ),
    BASS_CLEF(
        FClef(2), Note(NoteName.D, Octaves(3)),
        listOf(3, 3, 3, 3, 2, 3, 2), listOf(2, 3, 2, 3, 2, 3, 2)
    ),
    TENOR_CLEF(
        CClef(2), Note(NoteName.A, Octaves(3)),
        listOf(3, 4, 3, 4, 3, 4, 3), listOf(3, 4, 3, 4, 3, 4, 3)
    ),
}

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

    val pitch: Pitch
        get() = Pitch(note, accidental ?: keySignature.getAccidental(note.name))

    /**
     * The symbols that notate this representation.
     */
    fun produceSymbols(): List<Symbol> {
        val result = ArrayList<Symbol>()
        result.add(clef.symbol)
        result.addAll(getKeySignatureSymbols())
        val noteIndex = indexOfNote(note)
        accidental?.let {
            result.add(getAccidentalSymbol(it, noteIndex))
        }
        result.add(QuarterNote(noteIndex))
        return result
    }

    /**
     * The number of degrees in the C major scale that separate the given note and the
     * note that represents the middle line in the staff as set by the clef
     * equals the noteIndex at which the given note should be displayed in the staff.
     * @see StaffView.Staff.notePosition
     * @see Note.minus(rhs: Note)
     */
    private fun indexOfNote(note: Note): Int = (note - clef.middleNote).degrees

    private fun getKeySignatureSymbols() = sequence {
        val noteNames = keySignature.shiftedNoteNames
        val octaves = if (keySignature.sharpOrFlat) clef.sharpOctaves else clef.flatOctaves
        for (i in noteNames.indices) {
            val sigNote = Note(noteNames[i], Octaves(octaves[i]))
            val noteIndex = indexOfNote(sigNote)
            yield(
                if (keySignature.sharpOrFlat) SharpSignature(noteIndex)
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
