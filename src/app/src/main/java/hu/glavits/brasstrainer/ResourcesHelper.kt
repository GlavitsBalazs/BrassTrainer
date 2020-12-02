package hu.glavits.brasstrainer

import android.content.res.Resources
import androidx.annotation.StringRes
import hu.glavits.brasstrainer.game.DifficultySelection
import hu.glavits.brasstrainer.game.InstrumentSelection
import hu.glavits.brasstrainer.model.KeySignature

object ResourcesHelper {
    lateinit var resources: Resources

    fun getString(@StringRes id: Int) = resources.getString(id)

    fun getInstrumentName(ordinal: Int) =
        getInstrumentName(getEnumForOrdinal<InstrumentSelection>(ordinal))

    fun getInstrumentName(instrumentSelection: InstrumentSelection) =
        resources.getString(
            when (instrumentSelection) {
                InstrumentSelection.TRUMPET_B_FLAT -> R.string.trumpet_in_b_flat
                InstrumentSelection.TRUMPET_C -> R.string.trumpet_in_c
                InstrumentSelection.EUPHONIUM_B_FLAT -> R.string.euphonium_in_b_flat
                InstrumentSelection.EUPHONIUM_C -> R.string.euphonium_in_c
                InstrumentSelection.TUBA_B_FLAT -> R.string.tuba_in_b_flat
                InstrumentSelection.TUBA_C -> R.string.tuba_in_c
                InstrumentSelection.HORN_F -> R.string.horn_in_f
                else -> R.string.missing
            }
        )

    fun getKeySignatureName(ordinal: Int) =
        getKeySignatureName(getEnumForOrdinal<KeySignature>(ordinal))

    fun getKeySignatureName(keySignature: KeySignature) =
        resources.getString(
            when (keySignature) {
                KeySignature.C_FLAT_MAJOR -> R.string.c_flat_major
                KeySignature.G_FLAT_MAJOR -> R.string.g_flat_major
                KeySignature.D_FLAT_MAJOR -> R.string.d_flat_major
                KeySignature.A_FLAT_MAJOR -> R.string.a_flat_major
                KeySignature.E_FLAT_MAJOR -> R.string.e_flat_major
                KeySignature.B_FLAT_MAJOR -> R.string.b_flat_major
                KeySignature.F_MAJOR -> R.string.f_major
                KeySignature.C_MAJOR -> R.string.c_major
                KeySignature.G_MAJOR -> R.string.g_major
                KeySignature.D_MAJOR -> R.string.d_major
                KeySignature.A_MAJOR -> R.string.a_major
                KeySignature.E_MAJOR -> R.string.e_major
                KeySignature.B_MAJOR -> R.string.b_major
                KeySignature.F_SHARP_MAJOR -> R.string.f_sharp_major
                KeySignature.C_SHARP_MAJOR -> R.string.c_sharp_major
                else -> R.string.missing
            }
        )

    fun getDifficultyName(ordinal: Int) =
        getDifficultyName(getEnumForOrdinal<DifficultySelection>(ordinal))

    fun getDifficultyName(difficultySelection: DifficultySelection) =
        resources.getString(
            when (difficultySelection) {
                DifficultySelection.EASY -> R.string.easy
                DifficultySelection.NORMAL -> R.string.normal
                DifficultySelection.HARD -> R.string.hard
                else -> R.string.missing
            }
        )

    private inline fun <reified E : Enum<E>> getEnumForOrdinal(ordinal: Int): E =
        enumValues<E>()[ordinal]
}