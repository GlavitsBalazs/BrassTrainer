package hu.glavits.brasstrainer.scorekeeping

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.glavits.brasstrainer.R
import hu.glavits.brasstrainer.ResourcesHelper
import kotlinx.android.synthetic.main.item_high_score.view.*
import java.time.Instant
import java.time.format.DateTimeFormatter

class HighScoreAdapter : RecyclerView.Adapter<HighScoreAdapter.HighScoreViewHolder>() {
    private val items = mutableListOf<HighScore>()
    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighScoreViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_high_score, parent, false)
        return HighScoreViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HighScoreViewHolder, position: Int) {
        val item = items[position]
        holder.highScoreInstrument.text =
            ResourcesHelper.getInstrumentName(getEnumForOrdinal(item.instrumentSelection))
        holder.highScoreKeySignature.text = if (item.keySignature != null)
            ResourcesHelper.getKeySignatureName(getEnumForOrdinal(item.keySignature))
        else ResourcesHelper.getString(R.string.random)
        holder.highScoreDifficulty.text =
            ResourcesHelper.getDifficultyName(getEnumForOrdinal(item.difficultySelection))
        holder.highScoreTimestamp.text =
            DateTimeFormatter.ISO_INSTANT.format(Instant.ofEpochSecond(item.timestamp))
        holder.highScoreRatio.text =
            "${item.successCount} / ${item.successCount + item.failureCount}"
        holder.highScoreValue.text =
            item.score.toString()
    }

    inner class HighScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val highScoreInstrument: TextView = itemView.high_score_instrument
        val highScoreKeySignature: TextView = itemView.high_score_key_signature
        val highScoreDifficulty: TextView = itemView.high_score_difficulty
        val highScoreTimestamp: TextView = itemView.high_score_timestamp
        val highScoreRatio: TextView = itemView.high_score_ratio
        val highScoreValue: TextView = itemView.high_score_value
    }

    companion object {
        private inline fun <reified E : Enum<E>> getEnumForOrdinal(ordinal: Int): E =
            enumValues<E>()[ordinal]
    }
}