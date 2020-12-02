package hu.glavits.brasstrainer.game

import hu.glavits.brasstrainer.model.Accidental
import kotlin.math.round

/**
 * A strategy to get the accidental for each round.
 */
interface AccidentalGenerator {
    fun getAccidental(
        naturalIncluded: Boolean = false
    ): Accidental?
}

class NoAccidentals : AccidentalGenerator {
    override fun getAccidental(
        naturalIncluded: Boolean
    ): Accidental? = null
}

internal fun fillWithNulls(accidentals: MutableList<Accidental?>, chanceOfAccidental: Double) {
    val n = round(accidentals.size * (1 - chanceOfAccidental) / chanceOfAccidental).toInt()
    accidentals.addAll(List<Accidental?>(n) { null })
}

class RandomAccidentals : AccidentalGenerator {
    override fun getAccidental(
        naturalIncluded: Boolean
    ): Accidental? {
        val accidentals = mutableListOf<Accidental?>(Accidental.FLAT, Accidental.SHARP)
        if (naturalIncluded) accidentals.add(Accidental.NATURAL)
        fillWithNulls(accidentals, CHANCE_OF_ACCIDENTAL)
        return accidentals.random()
    }

    companion object {
        const val CHANCE_OF_ACCIDENTAL = 0.3
    }
}

class RandomDoubleAccidentals : AccidentalGenerator {
    override fun getAccidental(naturalIncluded: Boolean): Accidental? {
        val accidentals = mutableListOf<Accidental?>(
            Accidental.FLAT,
            Accidental.SHARP,
            Accidental.DOUBLE_FLAT,
            Accidental.DOUBLE_SHARP
        )
        if (naturalIncluded) accidentals.add(Accidental.NATURAL)
        fillWithNulls(accidentals, CHANCE_OF_ACCIDENTAL)
        return accidentals.random()
    }

    companion object {
        const val CHANCE_OF_ACCIDENTAL = 0.5
    }
}