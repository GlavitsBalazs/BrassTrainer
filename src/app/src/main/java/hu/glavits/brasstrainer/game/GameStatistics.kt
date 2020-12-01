package hu.glavits.brasstrainer.game

import java.time.Duration
import java.time.Instant
import kotlin.math.round
import kotlin.math.sqrt

class GameStatistics {
    var successCount: Int = 0
    var failureCount: Int = 0
    private var lastTimeStamp: Instant? = null
    private val durations = mutableListOf<Duration>()

    fun pause() {
        lastTimeStamp = null
    }

    fun start() {
        lastTimeStamp = Instant.now()
    }

    fun addResult(success: Boolean) {
        if (success) {
            val timestamp = Instant.now()
            if (lastTimeStamp != null)
                durations.add(Duration.between(lastTimeStamp, timestamp))
            lastTimeStamp = timestamp
            successCount++
        } else failureCount++
    }

    val score: Int
        get() = if (durations.size == 0) 0 else round(
            1000000 * wilsonScore(
                successCount.toDouble(),
                failureCount.toDouble()
            ) / durations.map { it.toMillis() }.average()
        ).toInt()

    override fun toString(): String {
        return "$successCount / ${successCount + failureCount}\n${score}"
    }

    companion object {
        /**
         * The lower bound of the Wilson score interval with a covering probability of 95%.
         * https://en.wikipedia.org/wiki/Binomial_proportion_confidence_interval
         * @param positive The number of positive trials
         * @param negative The number of negative trials
         * @return The probability of a positive trial.
         */
        private fun wilsonScore(positive: Double, negative: Double): Double {
            // covering probability = erf(sqrt(pseudoCount))
            val pseudoCount = 1.920729410347062979180688;
            val total = positive + negative;
            val x = total + 2.0 * pseudoCount;
            val mean = (positive + pseudoCount) / x;
            val y = pseudoCount + 2.0 * (positive * negative) / total;
            val confidence = sqrt(pseudoCount * y) / x;
            return mean - confidence;
        }
    }
}