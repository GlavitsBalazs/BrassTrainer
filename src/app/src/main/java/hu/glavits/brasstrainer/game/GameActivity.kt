package hu.glavits.brasstrainer.game

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import hu.glavits.brasstrainer.R
import hu.glavits.brasstrainer.model.*
import hu.glavits.brasstrainer.typesetting.Clef
import hu.glavits.brasstrainer.typesetting.StaffConfiguration
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {
    private var num: Int = 0
    private val keysFragment: KeysFragment =
        KeysFragment.newInstance(4)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        staffView.configuration = StaffConfiguration(
            Clef.TREBLE_CLEF,
            KeySignature.C_SHARP_MAJOR,
            Accidental.DOUBLE_FLAT,
            Note(NoteName.D, Octaves(3))
        )

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        supportActionBar?.hide()
        val trx = supportFragmentManager.beginTransaction()
        trx.add(R.id.keys_holder, keysFragment)
        trx.commit()

        textView.text = num.toString()
        button_air.setOnTouchListener { v, m ->
            when (m.action) {
                MotionEvent.ACTION_DOWN -> {
                    val k = keysToString(keysFragment.keys)
                    textView.text = "$k $num"
                    num++
                }
                MotionEvent.ACTION_UP -> {
                    textView.text = num.toString()
                }
            }
            return@setOnTouchListener false
        }
    }

    fun keysToString(keys: List<Boolean>): String {
        if (!keys.any { it }) return "0"
        var res = ""
        if (keys[0]) res += "1"
        if (keys[1]) res += "2"
        if (keys[2]) res += "3"
        if (keys.size > 3 && keys[3]) res += "4"
        return res
    }
}