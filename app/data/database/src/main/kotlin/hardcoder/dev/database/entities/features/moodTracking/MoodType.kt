package hardcoder.dev.database.entities.features.moodTracking

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_types")
data class MoodType(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mood_type_id")
    val id: Int = 0,
    val name: String,
    val iconId: Int,
    val positivePercentage: Int,
)
