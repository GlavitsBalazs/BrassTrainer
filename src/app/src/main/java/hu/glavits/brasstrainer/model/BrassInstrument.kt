package hu.glavits.brasstrainer.model

abstract class BrassInstrument {
    abstract val fundamentalTone: Int
    abstract val valves: List<Int>

    fun possibleTones(valveOffset: Int) =
        HARMONIC_SERIES.filterIndexed { i, _ -> !BAD_PARTIALS.contains(i) }
            .map { it + valveOffset }.toList()

    companion object {
        val HARMONIC_SERIES = listOf(
            0, 12, 19, 24, 28, 31, 34, 36, 38, 40, 42, 43,
            44, 46, 47, 48, 49, 50, 51, 52, 53, 54, 54, 55
        )

        val BAD_PARTIALS = listOf(7, 11, 13, 14, 21, 22, 23)
    }
}

class Trumpet : BrassInstrument() {
    override val fundamentalTone: Int = 46
    override val valves = listOf(-2, -1, -3)
}

class Euphonium : BrassInstrument() {
    override val fundamentalTone: Int = 34
    override val valves = listOf(-2, -1, -3, -4)
}

class Tuba : BrassInstrument() {
    override val fundamentalTone: Int = 22
    override val valves = listOf(-2, -1, -3, -4)
}

class Horn : BrassInstrument() {
    override val fundamentalTone: Int = 29
    override val valves = listOf(-3, -1, -2, 4)
}