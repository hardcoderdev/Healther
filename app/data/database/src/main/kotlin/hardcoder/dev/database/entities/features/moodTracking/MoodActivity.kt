package hardcoder.dev.database.entities.features.moodTracking

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_activities")
data class MoodActivity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mood_activity_id")
    val id: Int = 0,
    val name: String,
    val iconId: Int,
)
