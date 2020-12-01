package hu.glavits.brasstrainer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.glavits.brasstrainer.game.*
import hu.glavits.brasstrainer.model.KeySignature

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ResourcesHelper.resources = this.resources
        launchGame()
    }

    private fun launchGame() {
        val configBundle = Bundle()
        configBundle.putParcelable(GameActivity.GAME_CONFIGURATION, getConfig())
        startActivity(Intent(this@MainActivity, GameActivity::class.java), configBundle)
        resources
    }

    private fun getConfig(): GameConfiguration {
        return GameConfiguration(
            InstrumentSelection.TRUMPET_B_FLAT,
            KeySignature.C_MAJOR,
            DifficultySelection.EASY
        )
    }
}