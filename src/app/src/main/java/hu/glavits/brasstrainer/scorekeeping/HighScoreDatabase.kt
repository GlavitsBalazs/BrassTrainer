package hu.glavits.brasstrainer.scorekeeping

import android.content.Context
import androidx.annotation.NonNull
import androidx.room.*
import hu.glavits.brasstrainer.game.GameConfiguration
import hu.glavits.brasstrainer.game.GameStatistics
import java.time.Instant

@Entity(primaryKeys = ["instrumentSelection", "randomKeySignature", "keySignature", "difficultySelection"])
data class HighScore(
    val instrumentSelection: Int,
    val randomKeySignature: Boolean,
    val keySignature: Int,
    val difficultySelection: Int,
    val timestamp: Long,
    val successCount: Int,
    val failureCount: Int,
    val score: Int
) {
    constructor(
        gameConfiguration: GameConfiguration,
        gameStatistics: GameStatistics,
        timestamp: Instant
    ) : this(
        gameConfiguration.instrumentSelection.ordinal,
        gameConfiguration.keySignature == null,
        gameConfiguration.keySignature?.ordinal?: 0,
        gameConfiguration.difficultySelection.ordinal,
        timestamp.epochSecond,
        gameStatistics.successCount,
        gameStatistics.failureCount,
        gameStatistics.score
    )
}

@Dao
interface HighScoreDao {
    @Query("SELECT * FROM HighScore ORDER BY score DESC;")
    fun getAll(): List<HighScore>

    @Query(
        """SELECT * FROM HighScore
              WHERE instrumentSelection = :instrumentSelection
              AND randomKeySignature = :randomKeySignature
              AND keySignature = :keySignature
              AND difficultySelection = :difficultySelection
              LIMIT 1;"""
    )
    fun getSpecific(
        instrumentSelection: Int,
        randomKeySignature: Boolean,
        keySignature: Int,
        difficultySelection: Int
    ): HighScore?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(highScore: HighScore)
}

@Database(entities = [HighScore::class], version = 1)
abstract class HighScoreDatabase : RoomDatabase() {
    abstract fun getDao(): HighScoreDao

    fun saveHighScore(highScore: HighScore) {
        if (highScore.score == 0) return
        val dao = getDao()
        val current = dao.getSpecific(
            highScore.instrumentSelection,
            highScore.randomKeySignature,
            highScore.keySignature,
            highScore.difficultySelection
        )
        if (current == null || current.score <= highScore.score)
            dao.insert(highScore)
    }

    fun getAll() = getDao().getAll()

    companion object {
        var INSTANCE: HighScoreDatabase? = null
        private const val DB_NAME = "highscores.db"

        fun getInstance(context: Context): HighScoreDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    HighScoreDatabase::class.java,
                    DB_NAME
                ).build()
            }
            return INSTANCE!!
        }
    }
}