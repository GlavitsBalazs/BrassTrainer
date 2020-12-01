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
    EUPHONIUM_C(Euphonium(), EuphoniumClef(), Interval(0)),
    TUBA_B_FLAT(BFlatTuba(), TrebleClef(), Interval(28)),
    TUBA_C(BFlatTuba(), TubaClef(), Interval(0)),
    HORN_F(DoubleHorn(), HornClef(), Interval(7))
}

enum class DifficultySelection {
    EASY,
    NORMAL,
    HARD,
}

@Parcelize
data class GameConfiguration(
    val instrumentSelection: InstrumentSelection,
    val keySignature: KeySignature?,
    val difficultySelection: DifficultySelection
) : Parcelable