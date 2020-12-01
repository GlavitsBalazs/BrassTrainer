package hu.glavits.brasstrainer.game

import hu.glavits.brasstrainer.model.Note
import hu.glavits.brasstrainer.model.NoteName
import hu.glavits.brasstrainer.model.Octaves
import hu.glavits.brasstrainer.typesetting.Clef

abstract class ClefSelector {
    abstract fun getClef(note: Note): Clef
}

open class ConstantClef(private val clef: Clef) : ClefSelector() {
    override fun getClef(note: Note): Clef = clef
}

open class ClefCutoff(
    private val cutoff: Note,
    private val highClef: Clef,
    private val lowClef: Clef = Clef.BASS_CLEF
) : ClefSelector() {
    override fun getClef(note: Note): Clef =
        if (note >= cutoff) highClef else lowClef
}

class TrebleClef : ConstantClef(Clef.TREBLE_CLEF)

class BassClef : ConstantClef(Clef.BASS_CLEF)

class EuphoniumClef : ClefCutoff(Note(NoteName.G, Octaves(4)), Clef.TENOR_CLEF)

class TubaClef : ClefCutoff(Note(NoteName.D, Octaves(5)), Clef.TENOR_CLEF)

class HornClef : ClefCutoff(Note(NoteName.C, Octaves(3)), Clef.TREBLE_CLEF)