package hardcoder.dev.database

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import hardcoder.dev.database.appAdapters.AppPreferenceAdapters
import hardcoder.dev.database.appAdapters.DiaryAdapters
import hardcoder.dev.database.appAdapters.FoodTrackingAdapters
import hardcoder.dev.database.appAdapters.MoodTrackingAdapters
import hardcoder.dev.database.appAdapters.PedometerAdapters
import hardcoder.dev.database.appAdapters.UserAdapters
import hardcoder.dev.database.appAdapters.WaterTrackingAdapters

object AppDatabaseFactory {

    fun create(context: Context, name: String) = AppDatabase(
        driver = AndroidSqliteDriver(schema = AppDatabase.Schema, context = context, name = name),
        UserAdapter = UserAdapters.createUserAdapter(),
        AppPreferenceAdapter = AppPreferenceAdapters.createAppPreferenceAdapter(),
        WaterTrackAdapter = WaterTrackingAdapters.createWaterTrackAdapter(),
        DrinkTypeAdapter = WaterTrackingAdapters.createDrinkTypeAdapter(),
        PedometerTrackAdapter = PedometerAdapters.createPedometerTrackAdapter(),
        MoodWithActivityAdapter = MoodTrackingAdapters.createMoodWithActivityAdapter(),
        MoodTypeAdapter = MoodTrackingAdapters.createMoodTypeAdapter(),
        MoodActivityAdapter = MoodTrackingAdapters.createMoodActivityAdapter(),
        MoodTrackAdapter = MoodTrackingAdapters.createMoodTrackAdapter(),
        FoodTrackAdapter = FoodTrackingAdapters.createFoodTrackAdapter(),
        FoodTypeAdapter = FoodTrackingAdapters.createFoodTypeAdapter(),
        DiaryTagAdapter = DiaryAdapters.createDiaryTagAdapter(),
        DiaryAttachmentAdapter = DiaryAdapters.createDiaryAttachmentAdapter(),
        DiaryTrackAdapter = DiaryAdapters.createDiaryTrackAdapter(),
    )
}