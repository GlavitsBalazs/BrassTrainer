package hu.glavits.brasstrainer.game

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import hu.glavits.brasstrainer.R
import hu.glavits.brasstrainer.model.DoubleHorn
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {

    private lateinit var game: Game

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val configuration = savedInstanceState
            ?.getParcelable<GameConfiguration>(GAME_CONFIGURATION)
        requireNotNull(configuration) { "Missing game configuration." }
        game = Game(configuration)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        supportActionBar?.hide()

        setContentView(
            if (game.instrument is DoubleHorn)
                R.layout.activity_game_left_handed
            else R.layout.activity_game
        )

        val keysFragment = KeysFragment.newInstance(game.instrument.valveLabels)
        val trx = supportFragmentManager.beginTransaction()
        trx.add(R.id.keys_holder, keysFragment)
        trx.commit()

        button_air.setOnTouchListener { _, m ->
            when (m.action) {
                MotionEvent.ACTION_DOWN -> {
                    game.pressKeys(keysFragment.keys)
                    textView.text = game.stats.toString()
                }
                MotionEvent.ACTION_UP -> {
                    game.releaseKeys()
                    staffView.configuration = game.task
                }
            }
            return@setOnTouchListener false
        }

        game.start()
        staffView.configuration = game.task
        textView.text = game.stats.toString()
    }

    companion object {
        const val GAME_CONFIGURATION = "GameConfiguration"
    }
}