package hu.glavits.brasstrainer

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_trumpet_keys.*

class TrumpetKeysFragment : Fragment() {

    private var key_1_down = false
    private var key_2_down = false
    private var key_3_down = false

    val keys: List<Boolean>
        get() = listOf(key_1_down, key_2_down, key_3_down)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trumpet_keys, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        trumpet_key_1.setOnTouchListener { _, m ->
            when (m.action) {
                MotionEvent.ACTION_DOWN -> key_1_down = true
                MotionEvent.ACTION_UP -> key_1_down = false
            }
            return@setOnTouchListener false
        }
        trumpet_key_2.setOnTouchListener { _, m ->
            when (m.action) {
                MotionEvent.ACTION_DOWN -> key_2_down = true
                MotionEvent.ACTION_UP -> key_2_down = false
            }
            return@setOnTouchListener false
        }
        trumpet_key_3.setOnTouchListener { _, m ->
            when (m.action) {
                MotionEvent.ACTION_DOWN -> key_3_down = true
                MotionEvent.ACTION_UP -> key_3_down = false
            }
            return@setOnTouchListener false
        }
    }
}