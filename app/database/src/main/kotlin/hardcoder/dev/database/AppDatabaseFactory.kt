package hardcoder.dev.database

import android.content.Context
import com.squareup.sqldelight.EnumColumnAdapter
import com.squareup.sqldelight.android.AndroidSqliteDriver

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