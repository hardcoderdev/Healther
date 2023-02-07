package hardcoder.dev.healther.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import hardcoder.dev.healther.data.local.room.entities.WaterTrack

@Database(entities = [WaterTrack::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun waterTrackDao(): WaterTrackDao
}