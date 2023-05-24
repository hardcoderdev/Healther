package hardcoder.dev.database

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import hardcoder.dev.database.columnAdapters.InstantAdapter

object AppDatabaseFactory {

    fun create(context: Context, name: String) = AppDatabase(
        driver = AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = context,
            name = name
        ),
        AppPreferenceAdapter = AppPreference.Adapter(
            firstLaunchTimeAdapter = InstantAdapter,
            lastAppReviewRequestTimeAdapter = InstantAdapter
        ),
        WaterTrackAdapter = WaterTrack.Adapter(dateAdapter = InstantAdapter),
        PedometerTrackAdapter = PedometerTrack.Adapter(
            startTimeAdapter = InstantAdapter,
            endTimeAdapter = InstantAdapter
        ),
        FastingTrackAdapter = FastingTrack.Adapter(
            startTimeAdapter = InstantAdapter,
            interruptedTimeAdapter = InstantAdapter
        ),
        MoodTrackAdapter = MoodTrack.Adapter(dateAdapter = InstantAdapter),
        DiaryTrackAdapter = DiaryTrack.Adapter(dateAdapter = InstantAdapter)
    )
}