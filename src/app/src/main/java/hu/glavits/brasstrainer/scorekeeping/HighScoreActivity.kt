package hu.glavits.brasstrainer.scorekeeping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.glavits.brasstrainer.R
import kotlinx.android.synthetic.main.activity_high_score.*
import kotlin.concurrent.thread

class HighScoreActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HighScoreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_score)
        recyclerView = high_score_recycler_view
        adapter = HighScoreAdapter()
        loadItemsInBackground()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun loadItemsInBackground() {
        thread {
            val items = HighScoreDatabase.INSTANCE!!.getAll()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }
}