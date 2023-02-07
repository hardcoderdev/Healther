package hardcoder.dev.healther.di

import android.content.Context
import androidx.room.Room
import hardcoder.dev.healther.data.local.room.AppDatabase

class DatabaseModule(private val context: Context) {

    val appDatabase by lazy {
        Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "healther_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    val waterTrackDao by lazy {
        appDatabase.waterTrackDao()
    }
}