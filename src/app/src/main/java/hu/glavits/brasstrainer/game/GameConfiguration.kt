package hu.glavits.brasstrainer.game

import android.os.Parcelable
import hu.glavits.brasstrainer.model.*
import kotlinx.android.parcel.Parcelize

enum class InstrumentSelection(
    val instrument: BrassInstrument,
    val clefSelector: ClefSelector,
    val transposition: Interval
) {
    TRUMPET_B_FLAT(BFlatTrumpet(), TrebleClef(), Interval(2)),
    TRUMPET_C(BFlatTrumpet(), TrebleClef(), Interval(0)),
    EUPHONIUM_B_FLAT(Euphonium(), TrebleClef(), Interval(14)),
    BARITONE_B_FLAT(Euphonium(compensating = false), TrebleClef(), Interval(14)),
    EUPHONIUM_C(Euphonium(), LowBrassClef(), Interval(0)),
    TUBA_B_FLAT(BFlatTuba(), TrebleClef(), Interval(26)),
    TUBA_C(BFlatTuba(), LowBrassClef(), Interval(0)),
    HORN_F(DoubleHorn(), HornClef(), Interval(7))
}

enum class DifficultySelection {
    EASY,
    NORMAL,
    HARD,
}

/**
 * @param instrumentSelection Which instrument are we practicing? This also determines the clef and the transposition.
 * @param keySignature Null means random key signature each round.
 * @param difficultySelection Easy: no accidentals, Normal: random accidentals, Hard: double accidentals, extreme range
 */
@Parcelize
data class GameConfiguration(
    val instrumentSelection: InstrumentSelection,
    val keySignature: KeySignature?,
    val difficultySelection: DifficultySelection
) : Parcelable