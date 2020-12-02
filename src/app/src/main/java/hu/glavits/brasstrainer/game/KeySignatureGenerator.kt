package hu.glavits.brasstrainer.game

import hu.glavits.brasstrainer.model.KeySignature

/**
 * A strategy to get a key signature for each round.
 */
interface KeySignatureGenerator {
    fun getKeySignature(): KeySignature
}

class ConstantKeySignature(
    private val keySignature: KeySignature = KeySignature.C_MAJOR
) : KeySignatureGenerator {
    override fun getKeySignature(): KeySignature = keySignature
}

class RandomKeySignature : KeySignatureGenerator {
    override fun getKeySignature(): KeySignature = KeySignature.values().asList().random()
}