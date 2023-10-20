package hardcoder.dev.database.typeConverters

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class InstantTypeConverter {

    @TypeConverter
    fun to(databaseValue: Long) = Instant.fromEpochMilliseconds(databaseValue)

    @TypeConverter
    fun from(value: Instant) = value.toEpochMilliseconds()
}