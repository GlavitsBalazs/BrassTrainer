package hu.glavits.brasstrainer.game

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hu.glavits.brasstrainer.BuildConfig
import hu.glavits.brasstrainer.R

class KeysFragment : Fragment() {
    private var numKeys: Int? = null
    private var keyValues: Array<Boolean>? = null
    private var keyButtons: List<View>? = null

    val keys: List<Boolean>
        get() = List(numKeys!!) { keyValues!![it] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            numKeys = it.getInt(NUM_KEYS)
        }
        if (BuildConfig.DEBUG && !(numKeys == 3 || numKeys == 4)) {
            error("Invalid number of keys")
        }
        keyValues = Array(numKeys!!) { _ -> false }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout: Int = -1
        if (numKeys == 3) layout =
            R.layout.fragment_three_keys
        if (numKeys == 4) layout =
            R.layout.fragment_four_keys
        val view = inflater.inflate(layout, container, false)
        val fd: (Int) -> View = { view.findViewById(it) }
        keyButtons = keyIDs.map(fd).toList()
        return view

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        for (i in 0 until numKeys!!) {
            keyButtons?.get(i)?.setOnTouchListener { _, m ->
                when (m.action) {
                    MotionEvent.ACTION_DOWN -> keyValues?.set(i, true)
                    MotionEvent.ACTION_UP -> keyValues?.set(i, false)
                }
                return@setOnTouchListener false
            }
        }
    }

    companion object {
        private const val NUM_KEYS = "numKeys"
        private val keyIDs = listOf(
            R.id.trumpet_key_1,
            R.id.trumpet_key_2,
            R.id.trumpet_key_3,
            R.id.trumpet_key_4
        )

        @JvmStatic
        fun newInstance(numKeys: Int) =
            KeysFragment().apply {
                arguments = Bundle().apply {
                    putInt(NUM_KEYS, numKeys)
                    put
                }
            }
    }
}