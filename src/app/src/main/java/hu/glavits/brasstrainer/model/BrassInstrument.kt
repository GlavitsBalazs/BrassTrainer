package hu.glavits.brasstrainer.model

abstract class BrassInstrument {
    /**
     * This is the fundamental frequency of this instrument's harmonic series.
     * It is also the pitch of the lowest note playable in the open position.
     * The instrument is said to be pitched in the key of whichever note this pitch corresponds to.
     */
    abstract val fundamental: Pitch

    abstract val valveOffsets: Array<Interval>
    abstract val valveLabels: Array<String>

    protected abstract val lowestValveOffset: Interval
    open val fullRange: Pair<Pitch, Pitch>
        get() = Pair(fundamental - lowestValveOffset, fundamental + HARMONIC_SERIES.last())

    /**
     * Novice players might only be able to play these pitches.
     */
    abstract val noviceRange: Pair<Pitch, Pitch>

    /**
     * When valves are pressed, the instrument's harmonic series is shifted.
     * This function returns all the possible pitches that may sound with the given valve combination.
     */
    open fun possibleTones(valveOffset: Interval) =
        HARMONIC_SERIES.map { fundamental + it + valveOffset }.toList()

    companion object {
        /**
         * https://en.wikipedia.org/wiki/Harmonic_series AKA overtone series.
         * This is a physical property of all brass (and many other kinds of) instruments.
         * The values of the list represent the partials in increasing order, with each item
         * being by the number of semitones between its frequency and the fundamental.
         */
        private val FULL_HARMONIC_SERIES = listOf(
            0, 12, 19, 24, 28, 31, 34, 36, 38, 40, 42, 43,
            44, 46, 47, 48, 49, 50, 51, 52, 53, 54, 54, 55
        )

        /**
         * These partials are out of tune in twelve tone equal temperament.
         */
        private val INHARMONIC_PARTIALS = listOf(6, 10, 12, 13, 20, 21, 22)

        /**
         * Though technically possible, most players are unable to play partials higher than this.
         */
        private const val HIGHEST_PARTIAL = 16

        /**
         * Remove the inharmonic partials and the high partials.
         */
        val HARMONIC_SERIES = FULL_HARMONIC_SERIES.filterIndexed { i, _ ->
            i < HIGHEST_PARTIAL && !INHARMONIC_PARTIALS.contains(i)
        }.map { Interval(it) }.toList()
    }
}

abstract class ThreeValvedBrassInstrument : BrassInstrument() {
    override val valveOffsets = arrayOf(Interval(-2), Interval(-1), Interval(-3))
    override val valveLabels = arrayOf("1", "2", "3")
    override val lowestValveOffset: Interval = Interval(-6)
}

/**
 * @param compensating If true, the represented instrument is equipped with a mechanism that
 * mitigates the distortion of certain low notes.
 */
abstract class FourValvedBrassInstrument(private val compensating: Boolean = true) :
    BrassInstrument() {
    override val valveOffsets = arrayOf(Interval(-2), Interval(-1), Interval(-3), Interval(-5))
    override val valveLabels = arrayOf("1", "2", "3", "4")
    override val lowestValveOffset: Interval =
        if (compensating) Interval(-12) else Interval(-11)

    override fun possibleTones(valveOffset: Interval): List<Pitch> {
        return super.possibleTones(
            if (!compensating && valveOffset.semitones < -7)
                valveOffset + Interval(1)
            else valveOffset
        )
    }
}

class BFlatTrumpet : ThreeValvedBrassInstrument() {
    override val fundamental = Pitch(58) // Bb2
    override val noviceRange: Pair<Pitch, Pitch>
        get() = Pair(Pitch(Note(NoteName.E, Octaves(3))), Pitch(Note(NoteName.C, Octaves(6))))
}

class Euphonium(compensating: Boolean = true) : FourValvedBrassInstrument(compensating) {
    override val fundamental = Pitch(46) // Bb1
    override val noviceRange: Pair<Pitch, Pitch>
        get() = Pair(Pitch(Note(NoteName.C, Octaves(2))), Pitch(Note(NoteName.C, Octaves(5))))
}

class BFlatTuba(compensating: Boolean = true) : FourValvedBrassInstrument(compensating) {
    override val fundamental = Pitch(34) // Bb0
    override val noviceRange: Pair<Pitch, Pitch>
        get() = Pair(Pitch(Note(NoteName.E, Octaves(1))), Pitch(Note(NoteName.C, Octaves(4))))
}

/**
 * AKA French horn. Pitched in the key of F.
 * Played with the left hand, so the order of the keys is reversed.
 * The thumb lever transposes the instrument to the key of B flat.
 */
class DoubleHorn : BrassInstrument() {
    override val fundamental = Pitch(41) // F1
    override val valveOffsets = arrayOf(Interval(-3), Interval(-1), Interval(-2), Interval(5))
    override val valveLabels = arrayOf("3", "2", "1", "T")
    override val lowestValveOffset: Interval = Interval(6)
    override val fullRange: Pair<Pitch, Pitch>
        get() = Pair(super.fullRange.first, super.fullRange.second + valveOffsets[3])
    override val noviceRange: Pair<Pitch, Pitch>
        get() = Pair(Pitch(Note(NoteName.C, Octaves(3))), Pitch(Note(NoteName.F, Octaves(5))))
}