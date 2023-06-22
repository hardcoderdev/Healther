package hardcoder.dev.database

import android.content.Context
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import hardcoder.dev.database.columnAdapters.InstantAdapter

object AppDatabaseFactory {

    fun create(context: Context, name: String) = AppDatabase(
        driver = AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = context,
            name = name
        ),
        HeroAdapter = Hero.Adapter(
            idAdapter = IntColumnAdapter,
            weightAdapter = IntColumnAdapter,
            exerciseStressTimeAdapter = IntColumnAdapter,
            genderIdAdapter = IntColumnAdapter
        ),
        AppPreferenceAdapter = AppPreference.Adapter(
            firstLaunchTimeAdapter = InstantAdapter,
            lastAppReviewRequestTimeAdapter = InstantAdapter,
            idAdapter = IntColumnAdapter
        ),
        WaterTrackAdapter = WaterTrack.Adapter(
            dateAdapter = InstantAdapter,
            idAdapter = IntColumnAdapter,
            drinkTypeIdAdapter = IntColumnAdapter,
            millilitersCountAdapter = IntColumnAdapter
        ),
        DrinkTypeAdapter = DrinkType.Adapter(
            idAdapter = IntColumnAdapter,
            iconIdAdapter = IntColumnAdapter,
            hydrationIndexPercentageAdapter = IntColumnAdapter
        ),
        PedometerTrackAdapter = PedometerTrack.Adapter(
            startTimeAdapter = InstantAdapter,
            endTimeAdapter = InstantAdapter,
            idAdapter = IntColumnAdapter,
            stepsCountAdapter = IntColumnAdapter
        ),
        FastingTrackAdapter = FastingTrack.Adapter(
            startTimeAdapter = InstantAdapter,
            interruptedTimeAdapter = InstantAdapter,
            idAdapter = IntColumnAdapter,
            fastingPlanIdAdapter = IntColumnAdapter
        ),
        MoodTrackAdapter = MoodTrack.Adapter(
            dateAdapter = InstantAdapter,
            idAdapter = IntColumnAdapter,
            moodTypeIdAdapter = IntColumnAdapter
        ),
        MoodActivityAdapter = MoodActivity.Adapter(
            iconIdAdapter = IntColumnAdapter,
            idAdapter = IntColumnAdapter
        ),
        MoodTypeAdapter = MoodType.Adapter(
            idAdapter = IntColumnAdapter,
            iconIdAdapter = IntColumnAdapter,
            positivePercentageAdapter = IntColumnAdapter
        ),
        MoodWithActivityAdapter = MoodWithActivity.Adapter(
            idAdapter = IntColumnAdapter,
            activityIdAdapter = IntColumnAdapter,
            moodTrackIdAdapter = IntColumnAdapter
        ),
        DiaryTrackAdapter = DiaryTrack.Adapter(
            dateAdapter = InstantAdapter,
            idAdapter = IntColumnAdapter
        ),
        DiaryAttachmentAdapter = DiaryAttachment.Adapter(
            idAdapter = IntColumnAdapter,
            diaryTrackIdAdapter = IntColumnAdapter,
            targetIdAdapter = IntColumnAdapter,
            targetTypeIdAdapter = IntColumnAdapter
        ),
        DiaryTagAdapter = DiaryTag.Adapter(
            idAdapter = IntColumnAdapter,
            iconIdAdapter = IntColumnAdapter
        ),
    )
}