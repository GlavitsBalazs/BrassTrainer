package hu.glavits.brasstrainer.model

abstract class BrassInstrument {
    /**
     * This is the fundamental frequency of this instrument's harmonic series.
     * It is also the pitch of the lowest note playable in the open position.
     * The instrument is said to be pitched in the key of whichever note this pitch corresponds to.
     */
    abstract val fundamental: Pitch

    abstract val valveOffsets: List<Semitones>
    abstract val valveLabels: List<String>
    protected abstract val lowestValveOffset: Semitones
    open val fullRange: Pair<Pitch, Pitch>
        get() = Pair(fundamental - lowestValveOffset, fundamental + HARMONIC_SERIES.last())

    open val amateurRange: Pair<Pitch, Pitch>
        get() = Pair(Pitch(0), Pitch(0))

    open fun possibleTones(valveOffset: Semitones) =
        HARMONIC_SERIES.map { it + valveOffset }.toList()

    companion object {
        /**
         * https://en.wikipedia.org/wiki/Harmonic_series
         * This is a physical property of all brass (and many other kinds of) instruments.
         * The values of the list represent the partials in increasing order, with each partial
         * represented by the number of semitones between its frequency and the fundamental.
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
        private const val HIGHEST_PARTIAL = 11

        /**
         * Remove the inharmonic partials and the high partials.
         */
        val HARMONIC_SERIES = FULL_HARMONIC_SERIES.filterIndexed { i, _ ->
            i < HIGHEST_PARTIAL && !INHARMONIC_PARTIALS.contains(i)
        }.map { Semitones(it) }.toList()
    }
}

abstract class ThreeValvedBrassInstrument : BrassInstrument() {
    override val valveOffsets = listOf(Semitones(-2), Semitones(-1), Semitones(-3))
    override val valveLabels: List<String> = listOf("1", "2", "3")
    override val lowestValveOffset: Semitones = Semitones(-6)
}

/**
 * @param compensating If true, the represented instrument contains a mechanism that
 * mitigates the distortion of certain low notes.
 */
abstract class FourValvedBrassInstrument(private val compensating: Boolean = true) :
    BrassInstrument() {
    override val valveOffsets = listOf(Semitones(-2), Semitones(-1), Semitones(-3), Semitones(-5))
    override val valveLabels: List<String> = listOf("1", "2", "3", "4")
    override val lowestValveOffset: Semitones =
        if (compensating) Semitones(-12) else Semitones(-11)

    override fun possibleTones(valveOffset: Semitones): List<Semitones> {
        return super.possibleTones(
            if (!compensating && valveOffset.semitones < -7)
                valveOffset + Semitones(1)
            else valveOffset
        )
    }
}

class BFlatTrumpet : ThreeValvedBrassInstrument() {
    override val fundamental = Pitch(46) // Bb2
}

class Euphonium(compensating: Boolean = true) : FourValvedBrassInstrument(compensating) {
    override val fundamental = Pitch(34) // Bb1
}

class BFlatTuba(compensating: Boolean = true) : FourValvedBrassInstrument(compensating) {
    override val fundamental = Pitch(22) // Bb0
}

/**
 * AKA French horn. Pitched in the key of F.
 * Played with the left hand, so the order of the keys is reversed.
 * The thumb lever transposes the instrument to the key of B flat.
 */
class DoubleHorn : BrassInstrument() {
    override val fundamental = Pitch(29) //F1
    override val valveOffsets = listOf(Semitones(-3), Semitones(-1), Semitones(-2), Semitones(5))
    override val valveLabels: List<String> = listOf("3", "2", "1", "T")
    override val lowestValveOffset: Semitones = Semitones(6)
    override val fullRange: Pair<Pitch, Pitch>
        get() = Pair(super.fullRange.first, super.fullRange.second + valveOffsets[3])
}