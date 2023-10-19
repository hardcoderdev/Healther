package hardcoder.dev.database.entities.appPreferences

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(tableName = "app_preferences")
data class AppPreferences(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firstLaunchTime: Instant,
)
