package hu.glavits.brasstrainer.model

/**
 * The frequency of a musical tone.
 */
class Pitch {
    /**
     * The frequency of this pitch, encoded according to the
     * https://en.wikipedia.org/wiki/MIDI_tuning_standard
     */
    var frequency: Int = 0

    constructor(frequency: Int) {
        this.frequency = frequency
    }

    constructor(note: Note, accidental: Accidental? = null) {
        val pitchClass = note.name.offset + (accidental?.offset ?: 0)
        frequency = MIDI_C0 + NOTES_PER_OCTAVE * note.octave + pitchClass
    }

    val note: Note
        get() = Note(
            NOTE_NAMES_IN_OCTAVE[frequency % NOTES_PER_OCTAVE],
            (frequency - MIDI_C0) / NOTES_PER_OCTAVE
        )

    val accidental: Accidental
        get() = ACCIDENTALS_IN_OCTAVE[frequency % NOTES_PER_OCTAVE]

    companion object {
        const val MIDI_C0 = 24
        const val NOTES_PER_OCTAVE = 12

        val NOTE_NAMES_IN_OCTAVE = listOf(
            NoteName.C,
            NoteName.C,
            NoteName.D,
            NoteName.D,
            NoteName.E,
            NoteName.F,
            NoteName.F,
            NoteName.G,
            NoteName.G,
            NoteName.A,
            NoteName.A,
            NoteName.B
        )

        val ACCIDENTALS_IN_OCTAVE = listOf(
            Accidental.NATURAL,
            Accidental.SHARP,
            Accidental.NATURAL,
            Accidental.SHARP,
            Accidental.NATURAL,
            Accidental.NATURAL,
            Accidental.SHARP,
            Accidental.NATURAL,
            Accidental.SHARP,
            Accidental.NATURAL,
            Accidental.SHARP,
            Accidental.NATURAL
        )
    }
}