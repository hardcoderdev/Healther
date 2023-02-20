package hardcoder.dev.healther.data

import android.content.Context
import com.squareup.sqldelight.EnumColumnAdapter
import com.squareup.sqldelight.android.AndroidSqliteDriver
import hardcoder.dev.healther.database.AppDatabase
import hardcoder.dev.healther.database.Hero
import hardcoder.dev.healther.database.WaterTrack

object AppDatabaseFactory {

    fun create(context: Context, name: String) = AppDatabase(
        driver = AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = context,
            name = name
        ),
        waterTrackAdapter = WaterTrack.Adapter(
            drinkTypeAdapter = EnumColumnAdapter()
        ),
        HeroAdapter = Hero.Adapter(
            genderAdapter = EnumColumnAdapter()
        )
    )
}