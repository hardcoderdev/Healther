package hardcoder.dev.database.typeConverters

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.datetime.Instant

class InstantTypeConverter {

    @TypeConverter
    fun from(databaseValue: Long): Instant {
        return Instant.fromEpochMilliseconds(databaseValue)
    }

    @TypeConverters
    fun to(value: Instant): Long {
        return value.toEpochMilliseconds()
    }
}