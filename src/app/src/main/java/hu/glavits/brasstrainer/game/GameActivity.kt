package hu.glavits.brasstrainer.game

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hu.glavits.brasstrainer.R
import hu.glavits.brasstrainer.model.DoubleHorn
import hu.glavits.brasstrainer.scorekeeping.HighScore
import hu.glavits.brasstrainer.scorekeeping.HighScoreDatabase
import kotlinx.android.synthetic.main.activity_game.*
import java.time.Instant
import kotlin.concurrent.thread

class GameActivity : AppCompatActivity() {
    private lateinit var game: Game

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val configuration = this.intent.extras
            ?.getParcelable<GameConfiguration>(GAME_CONFIGURATION)
        requireNotNull(configuration) { "Missing game configuration." }
        game = Game(configuration)

        setContentView(
            if (game.instrument is DoubleHorn) // The french horn is played with the left hand.
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
    }

    override fun onStart() {
        super.onStart()
        game.start()
        staffView.configuration = game.task
        textView.text = game.stats.toString()
    }

    override fun onStop() {
        super.onStop()
        game.pause()
    }

    private var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {
        if (!doubleBackToExitPressedOnce) {
            doubleBackToExitPressedOnce = true
            Toast.makeText(
                this, getString(R.string.double_back_exit_prompt),
                Toast.LENGTH_SHORT
            ).show()
            Handler(Looper.getMainLooper()).postDelayed(
                { doubleBackToExitPressedOnce = false },
                2000
            )
        } else super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        thread { // Room doesn't allow database access from the UI thread.
            HighScoreDatabase.INSTANCE?.saveHighScore(
                HighScore(
                    game.config, game.stats, Instant.now()
                )
            )
        }
    }

    companion object {
        const val GAME_CONFIGURATION = "GameConfiguration"
    }
}