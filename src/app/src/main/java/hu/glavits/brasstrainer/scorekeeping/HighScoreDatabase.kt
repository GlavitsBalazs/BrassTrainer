package hu.glavits.brasstrainer.scorekeeping

import android.content.Context
import androidx.room.*
import hu.glavits.brasstrainer.game.GameConfiguration
import hu.glavits.brasstrainer.game.GameStatistics
import java.time.Instant

@Entity(primaryKeys = ["instrumentSelection", "keySignature", "difficultySelection"])
data class HighScore(
    val instrumentSelection: Int,
    val keySignature: Int?,
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
        gameConfiguration.keySignature?.ordinal,
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

    @Query("""SELECT * FROM HighScore
              WHERE instrumentSelection = :instrumentSelection
              AND keySignature = :keySignature
              AND difficultySelection = :difficultySelection
              LIMIT 1;""")
    fun getSpecific(
        instrumentSelection: Int,
        keySignature: Int?,
        difficultySelection: Int
    ): HighScore?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(highScore: HighScore)

    @Update
    fun update(highScore: HighScore)

    @Delete
    fun deleteItem(highScore: HighScore)
}

@Database(entities = [HighScore::class], version = 1)
abstract class HighScoreDatabase : RoomDatabase() {
    abstract fun highScoreDao(): HighScoreDao

    fun saveHighScore(highScore: HighScore) {
        val dao = highScoreDao()
        val current = dao.getSpecific(
            highScore.instrumentSelection,
            highScore.keySignature,
            highScore.difficultySelection
        )
        if (current == null || current.score <= highScore.score)
            dao.insert(highScore)
    }

    companion object {
        var INSTANCE: HighScoreDatabase? = null

        fun getInstance(context: Context): HighScoreDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    HighScoreDatabase::class.java,
                    "highscores.db"
                ).build()
            }
            return INSTANCE!!
        }
    }
}