package hu.glavits.brasstrainer.game

import hu.glavits.brasstrainer.model.Note
import hu.glavits.brasstrainer.model.NoteName
import hu.glavits.brasstrainer.model.Octaves
import hu.glavits.brasstrainer.typesetting.Clef
import hu.glavits.brasstrainer.typesetting.StaffConfiguration

class GameConfiguration {

}

class Game {
    fun nextTask(): StaffConfiguration {
        return StaffConfiguration(Clef.TREBLE_CLEF, Note(NoteName.A, Octaves(4)))
    }
}