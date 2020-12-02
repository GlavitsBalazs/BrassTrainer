package hu.glavits.brasstrainer.game

import hu.glavits.brasstrainer.model.*
import kotlin.random.Random

/**
 * A strategy to get a note for each round.
 */
interface NoteGenerator {
    fun getNote(accidental: Accidental = Accidental.NATURAL): Note
    fun getNote(keySignature: KeySignature): Note
}

class RandomNoteGenerator(private val range: Pair<Pitch, Pitch>) : NoteGenerator {

    /**
     * Generate a random note that will fit in the range with the given accidental.
     */
    override fun getNote(accidental: Accidental): Note {
        val min = ceil(range.first - accidental.offset)
        val max = floor(range.second - accidental.offset)
        return getNote(min, max)
    }

    /**
     * Generate a random note that will fit in the range with the given key signature.
     */
    override fun getNote(keySignature: KeySignature): Note {
        var min = ceil(range.first)
        var max = floor(range.second)
        min = floor(Pitch(min) - keySignature.getAccidental(min.name).offset)
        max = floor(Pitch(max) - keySignature.getAccidental(max.name).offset)
        return getNote(min, max)
    }

    private fun getNote(min: Note, max: Note): Note {
        val diff = max - min
        return min + Degrees(Random.nextInt(diff.degrees + 1))
    }
}