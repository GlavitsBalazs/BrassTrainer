package hu.glavits.brasstrainer.model


/**
 * @param offset The number of half steps from C.
 * @param degree Degree in the C major scale.
 */
enum class NoteName(val offset: Int, val degree: Int) {
    C(0, 0),
    D(2, 1),
    E(4, 2),
    F(5, 3),
    G(7, 4),
    A(9, 5),
    B(11, 6);

    /**
     * @param rhs The number degrees in the major scale to shift by.
     */
    operator fun plus(rhs: Int): NoteName {
        val values = values()
        return values[(values.indexOf(this) + rhs) % values.count()]
    }

    operator fun minus(rhs: Int): NoteName = this + (-rhs)
}

/**
 * @param offset The number of half steps by which this accidental shifts the pitch.
 */
enum class Accidental(val offset: Int) {
    FLAT(-1),
    NATURAL(0),
    SHARP(1),
}


/**
 * @param octave The octave of this note, according to scientific pitch notation.
 */
class Note(val name: NoteName, val octave: Int) {
    /**
     * Diatonc transposition.
     * @param rhs The number degrees in the major scale to shift.
     */
    operator fun plus(rhs: Int): Note = Note(
        scale[(name.degree + rhs) % scale.count()],
        octave + (name.degree + rhs) / scale.count()
    )

    operator fun minus(rhs: Int): Note = this + (-rhs)

    /**
     * The number of major scale degrees separating the notes.
     */
    operator fun minus(rhs: Note): Int =
        (octave * scale.count() + name.degree) - (rhs.octave * scale.count() + rhs.name.degree)

    companion object {
        val scale = NoteName.values()
    }
}