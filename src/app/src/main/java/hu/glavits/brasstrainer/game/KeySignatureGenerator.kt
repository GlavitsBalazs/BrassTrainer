package hu.glavits.brasstrainer.game

import hu.glavits.brasstrainer.model.KeySignature

abstract class KeySignatureGenerator {
    abstract fun getKeySignature(): KeySignature
}

class ConstantKeySignature(
    private val keySignature: KeySignature = KeySignature.C_MAJOR
) : KeySignatureGenerator() {
    override fun getKeySignature(): KeySignature = keySignature
}

class RandomKeySignature() : KeySignatureGenerator() {
    override fun getKeySignature(): KeySignature = KeySignature.values().asList().random()
}