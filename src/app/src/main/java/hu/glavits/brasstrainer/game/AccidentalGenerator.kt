package hu.glavits.brasstrainer.game

import hu.glavits.brasstrainer.model.Accidental

abstract class AccidentalGenerator {
    abstract fun getAccidental(
        naturalIncluded: Boolean = false
    ): Accidental?
}

class NoAccidentals : AccidentalGenerator() {
    override fun getAccidental(
        naturalIncluded: Boolean
    ): Accidental? = null
}

class RandomAccidentals : AccidentalGenerator() {
    override fun getAccidental(
        naturalIncluded: Boolean
    ): Accidental? {
        val accidentals = mutableListOf(Accidental.FLAT, Accidental.SHARP)
        if (naturalIncluded) accidentals.add(Accidental.NATURAL)
        return accidentals.random()
    }
}

class RandomDoubleAccidentals : AccidentalGenerator() {
    override fun getAccidental(naturalIncluded: Boolean): Accidental? {
        val accidentals = mutableListOf(Accidental.FLAT, Accidental.SHARP, Accidental.DOUBLE_FLAT, Accidental.DOUBLE_SHARP)
        if (naturalIncluded) accidentals.add(Accidental.NATURAL)
        return accidentals.random()
    }
}