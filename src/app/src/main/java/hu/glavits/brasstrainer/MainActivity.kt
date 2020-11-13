package hu.glavits.brasstrainer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import hu.glavits.brasstrainer.model.Clef
import hu.glavits.brasstrainer.model.KeySignature
import hu.glavits.brasstrainer.model.Note
import hu.glavits.brasstrainer.model.NoteName
import hu.glavits.brasstrainer.typesetting.StaffConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_trumpet_keys.*

class MainActivity : AppCompatActivity() {
    var num: Int = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        textView.text = num.toString()
//        button_air.setOnTouchListener { v, m ->
//            when (m.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    val k = keysToString((keys_fragment as TrumpetKeysFragment).keys)
//                    textView.text = "$k $num"
//                    num++
//                }
//                MotionEvent.ACTION_UP -> {
//                    textView.text = num.toString()
//                }
//            }
//            return@setOnTouchListener false
//        }
    }

    fun keysToString(keys: List<Boolean>): String {
        if (!keys.any { it }) return "0"
        var res = ""
        if (keys[0]) res += "1"
        if (keys[1]) res += "2"
        if (keys[2]) res += "3"
        return res
    }
}