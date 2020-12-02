package hu.glavits.brasstrainer

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import hu.glavits.brasstrainer.game.DifficultySelection
import hu.glavits.brasstrainer.game.GameActivity
import hu.glavits.brasstrainer.game.GameConfiguration
import hu.glavits.brasstrainer.game.InstrumentSelection
import hu.glavits.brasstrainer.model.KeySignature
import hu.glavits.brasstrainer.scorekeeping.HighScoreActivity
import hu.glavits.brasstrainer.scorekeeping.HighScoreDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_main)

        ResourcesHelper.resources = this.resources
        HighScoreDatabase.getInstance(this)

        instrument_spinner.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                InstrumentSelection.values().map(ResourcesHelper::getInstrumentName)
            ).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        instrument_spinner.setSelection(0)

        signature_spinner.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                listOf(getString(R.string.random)).plus(
                    KeySignature.values().map(ResourcesHelper::getKeySignatureName)
                )
            ).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        signature_spinner.setSelection(0)

        difficulty_spinner.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                DifficultySelection.values().map(ResourcesHelper::getDifficultyName)
            ).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        difficulty_spinner.setSelection(0)

        button_start_game.setOnClickListener {
            val intent = Intent(this@MainActivity, GameActivity::class.java)
            intent.putExtra(GameActivity.GAME_CONFIGURATION, getConfig())
            startActivity(intent)
        }

        button_high_scores.setOnClickListener {
            startActivity(Intent(this@MainActivity, HighScoreActivity::class.java))
        }
    }

    private fun getConfig(): GameConfiguration {
        return GameConfiguration(
            InstrumentSelection.values()[instrument_spinner.selectedItemPosition],
            if (signature_spinner.selectedItemPosition != 0)
                KeySignature.values()[signature_spinner.selectedItemPosition - 1]
            else null,
            DifficultySelection.values()[difficulty_spinner.selectedItemPosition]
        )
    }
}