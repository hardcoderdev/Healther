package hardcoder.dev.database.entities.features.pedometer

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(tableName = "pedometer_tracks")
data class PedometerTrack(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val stepsCount: Int,
    val startTime: Instant,
    val endTime: Instant,
)
