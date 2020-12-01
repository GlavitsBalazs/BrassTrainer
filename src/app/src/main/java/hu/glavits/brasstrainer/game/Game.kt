package hu.glavits.brasstrainer.game

import hu.glavits.brasstrainer.model.*
import hu.glavits.brasstrainer.typesetting.StaffConfiguration

class Game(val config: GameConfiguration) {
    val instrument: BrassInstrument
    val transposition: Interval
    val clefSelector: ClefSelector
    val range: Pair<Pitch, Pitch>
    val keySignatureGenerator: KeySignatureGenerator
    val accidentalGenerator: AccidentalGenerator
    val noteGenerator: NoteGenerator
    val stats: GameStatistics

    init {
        instrument = config.instrumentSelection.instrument
        transposition = config.instrumentSelection.transposition
        clefSelector = config.instrumentSelection.clefSelector
        keySignatureGenerator = when (config.keySignature) {
            null -> RandomKeySignature()
            else -> ConstantKeySignature(config.keySignature)
        }
        range = when (config.difficultySelection) {
            DifficultySelection.EASY -> instrument.noviceRange
            DifficultySelection.NORMAL -> instrument.noviceRange
            DifficultySelection.HARD -> instrument.fullRange
        }
        accidentalGenerator = when (config.difficultySelection) {
            DifficultySelection.EASY -> NoAccidentals()
            DifficultySelection.NORMAL -> RandomAccidentals()
            DifficultySelection.HARD -> RandomDoubleAccidentals()
        }
        noteGenerator = RandomNoteGenerator(
            Pair(range.first + transposition, range.second + transposition)
        )
        stats = GameStatistics()
    }

    var task: StaffConfiguration? = null
    var lastResult: Boolean? = null

    fun start() {
        task = nextTask()
        // stats.start()
    }

    fun pause() {
        stats.pause()
    }

    fun pressKeys(keys: List<Boolean>) {
        val result = evaluateKeys(keys)
        lastResult = result
        stats.addResult(result)
    }

    fun releaseKeys() {
        if (lastResult == true) task = nextTask()
    }

    private fun evaluateKeys(keys: List<Boolean>): Boolean {
        var offset = Interval(0)
        for (i in keys.indices)
            if (keys[i])
                offset += instrument.valveOffsets[i]
        val possibilities = instrument.possibleTones(offset)
        if (task != null) {
            val pitch = task!!.pitch - transposition
            return possibilities.contains(pitch)
        }
        return false
    }

    private fun nextTask(): StaffConfiguration {
        val keySignature = keySignatureGenerator.getKeySignature()
        val accidental = accidentalGenerator.getAccidental()
        val note = if (accidental != null) noteGenerator.getNote(accidental)
        else noteGenerator.getNote(keySignature)
        val clef = clefSelector.getClef(note)
        return StaffConfiguration(clef, keySignature, accidental, note)
    }
}