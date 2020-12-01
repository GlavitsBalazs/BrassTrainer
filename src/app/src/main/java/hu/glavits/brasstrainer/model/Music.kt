package hu.glavits.brasstrainer.model

import hu.glavits.brasstrainer.model.Octaves.Companion.SEMITONES_PER_OCTAVE
import kotlin.math.abs

/**
 * The frequency of a musical tone.
 */
class Pitch : Comparable<Pitch> {
    /**
     * The frequency of this pitch, encoded according to the
     * https://en.wikipedia.org/wiki/MIDI_tuning_standard
     */
    var frequency: Int = 0

    constructor(frequency: Int) {
        this.frequency = frequency
    }

    constructor(note: Note, accidental: Accidental = Accidental.NATURAL) {
        val pitchClass = note.name.pitchClass + accidental.offset
        frequency = (MIDI_C0 + (note.octaves + pitchClass)).frequency
    }

    // The interval from the nearest C below.
    val pitchClass: Interval
        get() = Interval(frequency % SEMITONES_PER_OCTAVE)

    val note: Note
        get() = Note(
            NOTE_NAMES_IN_OCTAVE[pitchClass.semitones],
            Octaves(this - MIDI_C0)
        )

    val accidental: Accidental
        get() = ACCIDENTALS_IN_OCTAVE[pitchClass.semitones]

    operator fun plus(rhs: Interval): Pitch = Pitch(frequency + rhs.semitones)
    operator fun minus(rhs: Interval): Pitch = this + (-rhs)

    operator fun minus(rhs: Pitch): Interval = Interval(frequency - rhs.frequency)

    override operator fun compareTo(other: Pitch) = frequency.compareTo(other.frequency)

    override fun equals(other: Any?): Boolean = (other is Pitch) && this.compareTo(other) == 0
    override fun hashCode(): Int = frequency

    companion object {
        val MIDI_C0 = Pitch(24)

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

/**
 * Find the closest note above.
 */
fun ceil(pitch: Pitch): Note {
    return pitch.note + (if (pitch.accidental == Accidental.SHARP) Degrees(1) else Degrees(0))
}

/**
 * Find the closest note below.
 */
fun floor(pitch: Pitch): Note {
    return pitch.note
}

fun ceil(note: Note, accidental: Accidental): Note = ceil(Pitch(note, accidental))

fun floor(note: Note, accidental: Accidental): Note = floor(Pitch(note, accidental))

/**
 * The distance between musical frequencies expressed in terms of semitones.
 */
open class Interval(val semitones: Int) : Comparable<Interval> {
    val octaves: Int
        get() = semitones / SEMITONES_PER_OCTAVE

    operator fun plus(rhs: Interval): Interval = Interval(semitones + rhs.semitones)
    operator fun minus(rhs: Interval): Interval = this + (-rhs)
    operator fun unaryMinus(): Interval = Interval(-semitones)
    override operator fun compareTo(other: Interval) = semitones.compareTo(other.semitones)
}

/**
 * 12 semitones.
 */
class Octaves(octaves: Int) : Interval(octaves * SEMITONES_PER_OCTAVE) {
    constructor(interval: Interval) : this(interval.semitones / SEMITONES_PER_OCTAVE)

    companion object {
        const val SEMITONES_PER_OCTAVE = 12
    }
}

/**
 * Represents degree in the C major scale.
 */
class Degrees(val degrees: Int) : Comparable<Degrees> {
    constructor(octaves: Octaves) : this(octaves.octaves * DEGREES_PER_OCTAVE)

    val octaves: Octaves
        get() = Octaves(degrees / DEGREES_PER_OCTAVE)

    val name: NoteName
        get() = C_MAJOR[degrees % DEGREES_PER_OCTAVE]

    val interval: Interval
        get() = octaves + name.pitchClass

    operator fun plus(rhs: Degrees): Degrees = Degrees(degrees + rhs.degrees)
    operator fun minus(rhs: Degrees): Degrees = this + (-rhs)
    operator fun unaryMinus(): Degrees = Degrees(-degrees)

    override operator fun compareTo(other: Degrees) = degrees.compareTo(other.degrees)

    companion object {
        val C_MAJOR
            get() = NoteName.values()
        const val DEGREES_PER_OCTAVE = 7
    }
}

/**
 * @param degrees Degree in the C major scale.
 * @param pitchClass The number of semitones from C.
 */
enum class NoteName(val degrees: Degrees, val pitchClass: Interval) {
    C(Degrees(0), Interval(0)),
    D(Degrees(1), Interval(2)),
    E(Degrees(2), Interval(4)),
    F(Degrees(3), Interval(5)),
    G(Degrees(4), Interval(7)),
    A(Degrees(5), Interval(9)),
    B(Degrees(6), Interval(11));
}

/**
 * @param octaves The octave of this note, according to scientific pitch notation.
 *                i.e. the number of octaves from C0.
 * @see Pitch.MIDI_C0
 */
class Note(val name: NoteName, val octaves: Octaves) : Comparable<Note> {
    /**
     * Diatonic transposition.
     * @param rhs The number degrees in the major scale to shift by.
     */
    operator fun plus(rhs: Degrees): Note = Note(
        (name.degrees + rhs).name,
        Octaves(octaves + (name.degrees + rhs).octaves)
    )

    operator fun minus(rhs: Degrees): Note = this + (-rhs)


    /**
     * The number of degrees away from C0.
     */
    private val absoluteDegrees: Degrees
        get() = Degrees(octaves) + name.degrees

    /**
     * The number of major scale degrees separating the notes.
     */
    operator fun minus(rhs: Note): Degrees = absoluteDegrees - rhs.absoluteDegrees

    override operator fun compareTo(other: Note) = absoluteDegrees.compareTo(other.absoluteDegrees)
}

/**
 * An accidental is a symbol that modifies the pitch of notes.
 * @param offset The number of semitones by which this accidental shifts the pitch.
 */
enum class Accidental(val offset: Interval) {
    DOUBLE_FLAT(Interval(-2)),
    FLAT(Interval(-1)),
    NATURAL(Interval(0)),
    SHARP(Interval(1)),
    DOUBLE_SHARP(Interval(2)),
}

/**
 * @param accidentals The number of accidentals.
 *  Negative accidentals mean flats, positive means sharps.
 */
enum class KeySignature(private val accidentals: Int) {
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

    val sharpOrFlat: Boolean
        get() = accidentals > 0

    val shiftedNoteNames: List<NoteName>
        get() {
            if (accidentals == 0) return listOf()
            val notes = if (sharpOrFlat) SHARP_NOTES else FLAT_NOTES
            return notes.subList(0, abs(accidentals))
        }

    /**
     * Is the note flat or sharp in this key signature?
     */
    fun getAccidental(name: NoteName): Accidental {
        if (shiftedNoteNames.contains(name))
            return if (sharpOrFlat) Accidental.SHARP else Accidental.FLAT
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