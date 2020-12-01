package hu.glavits.brasstrainer

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.glavits.brasstrainer.game.*
import hu.glavits.brasstrainer.model.KeySignature
import hu.glavits.brasstrainer.scorekeeping.HighScoreActivity
import hu.glavits.brasstrainer.scorekeeping.HighScoreDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_main)

        //supportActionBar?.hide()

        ResourcesHelper.resources = this.resources
        HighScoreDatabase.getInstance(this)

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
            InstrumentSelection.HORN_F,
            KeySignature.D_MAJOR,
            DifficultySelection.EASY
        )
    }
}