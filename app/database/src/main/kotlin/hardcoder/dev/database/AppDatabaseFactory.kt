package hardcoder.dev.database

import android.content.Context

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import hardcoder.dev.database.appAdapters.AppPreferenceAdapters
import hardcoder.dev.database.appAdapters.DailyStreakAdapter
import hardcoder.dev.database.appAdapters.DiaryAdapters
import hardcoder.dev.database.appAdapters.FastingAdapters
import hardcoder.dev.database.appAdapters.HeroAdapters
import hardcoder.dev.database.appAdapters.MoodTrackingAdapters
import hardcoder.dev.database.appAdapters.PedometerAdapters
import hardcoder.dev.database.appAdapters.WalletAdapter
import hardcoder.dev.database.appAdapters.WaterTrackingAdapters

object AppDatabaseFactory {

    fun create(context: Context, name: String) = AppDatabase(
        driver = AndroidSqliteDriver(schema = AppDatabase.Schema, context = context, name = name),
        HeroAdapter = HeroAdapters.createHeroAdapter(),
        AppPreferenceAdapter = AppPreferenceAdapters.createAppPreferenceAdapter(),
        WaterTrackAdapter = WaterTrackingAdapters.createWaterTrackAdapter(),
        DrinkTypeAdapter = WaterTrackingAdapters.createDrinkTypeAdapter(),
        PedometerTrackAdapter = PedometerAdapters.createPedometerTrackAdapter(),
        FastingTrackAdapter = FastingAdapters.createFastingTrackAdapter(),
        MoodWithActivityAdapter = MoodTrackingAdapters.createMoodWithActivityAdapter(),
        MoodTypeAdapter = MoodTrackingAdapters.createMoodTypeAdapter(),
        MoodActivityAdapter = MoodTrackingAdapters.createMoodActivityAdapter(),
        MoodTrackAdapter = MoodTrackingAdapters.createMoodTrackAdapter(),
        DiaryTagAdapter = DiaryAdapters.createDiaryTagAdapter(),
        DiaryAttachmentAdapter = DiaryAdapters.createDiaryAttachmentAdapter(),
        DiaryTrackAdapter = DiaryAdapters.createDiaryTrackAdapter(),
        WalletAdapter = WalletAdapter.createWalletAdapter(),
        DailyStreakAdapter = DailyStreakAdapter.createDailyStreakAdapter(),
    )
}