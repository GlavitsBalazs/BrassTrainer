package hu.glavits.brasstrainer.game

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import hu.glavits.brasstrainer.R

/**
 * This fragment contains the buttons that represent the instrument keys.
 */
class KeysFragment : Fragment() {
    private lateinit var keyLabels: Array<String>
    private lateinit var keyValues: Array<Boolean>
    private lateinit var keyButtons: List<Button>

    val keys: List<Boolean>
        get() = keyValues.asList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keyLabels = arguments?.getStringArray(KEY_LABELS)!!
        require(keyLabels.size == 3 || keyLabels.size == 4) { "Invalid number of keys" }
        keyValues = Array(keyLabels.size) { false }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout: Int = -1
        if (keyLabels.size == 3) layout = R.layout.fragment_three_keys
        if (keyLabels.size == 4) layout = R.layout.fragment_four_keys
        val view = inflater.inflate(layout, container, false)
        val findButton: (Int) -> Button = { view.findViewById(it) }
        keyButtons = keyIDs.subList(0, keyLabels.size).map(findButton).toList()
        for (i in keyButtons.indices)
            keyButtons[i].text = keyLabels[i]
        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        for (i in keyLabels.indices) {
            keyButtons[i].setOnTouchListener { _, m ->
                when (m.action) {
                    MotionEvent.ACTION_DOWN -> keyValues[i] = true
                    MotionEvent.ACTION_UP -> keyValues[i] = false
                }
                return@setOnTouchListener false
            }
        }
    }

    companion object {
        private const val NUM_KEYS = "numKeys"
        private const val KEY_LABELS = "keyLabels"
        private val keyIDs = listOf(
            R.id.instrument_key_1,
            R.id.instrument_key_2,
            R.id.instrument_key_3,
            R.id.instrument_key_4
        )

        @JvmStatic
        fun newInstance(keyLabels: Array<String>) =
            KeysFragment().apply {
                arguments = Bundle().apply {
                    putInt(NUM_KEYS, keyLabels.size)
                    putStringArray(KEY_LABELS, keyLabels)
                }
            }
    }
}