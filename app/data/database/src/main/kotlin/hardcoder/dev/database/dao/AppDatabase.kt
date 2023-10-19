package hardcoder.dev.database.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hardcoder.dev.database.dao.appPreferences.AppPreferencesDao
import hardcoder.dev.database.dao.features.diary.DiaryAttachmentDao
import hardcoder.dev.database.dao.features.diary.DiaryTagDao
import hardcoder.dev.database.dao.features.diary.DiaryTrackDao
import hardcoder.dev.database.dao.features.foodTracking.FoodTrackDao
import hardcoder.dev.database.dao.features.foodTracking.FoodTypeDao
import hardcoder.dev.database.dao.features.moodTracking.MoodActivityDao
import hardcoder.dev.database.dao.features.moodTracking.MoodTrackDao
import hardcoder.dev.database.dao.features.moodTracking.MoodTypeDao
import hardcoder.dev.database.dao.features.moodTracking.MoodWithActivityDao
import hardcoder.dev.database.dao.features.pedometer.PedometerTrackDao
import hardcoder.dev.database.dao.features.waterTracking.DrinkTypeDao
import hardcoder.dev.database.dao.features.waterTracking.WaterTrackDao
import hardcoder.dev.database.dao.user.UserDao
import hardcoder.dev.database.entities.appPreferences.AppPreferences
import hardcoder.dev.database.entities.features.diary.DiaryAttachment
import hardcoder.dev.database.entities.features.diary.DiaryTag
import hardcoder.dev.database.entities.features.diary.DiaryTrack
import hardcoder.dev.database.entities.features.foodTracking.FoodTrack
import hardcoder.dev.database.entities.features.foodTracking.FoodType
import hardcoder.dev.database.entities.features.moodTracking.MoodActivity
import hardcoder.dev.database.entities.features.moodTracking.MoodActivityCrossRef
import hardcoder.dev.database.entities.features.moodTracking.MoodTrack
import hardcoder.dev.database.entities.features.moodTracking.MoodType
import hardcoder.dev.database.entities.features.pedometer.PedometerTrack
import hardcoder.dev.database.entities.features.waterTracking.DrinkType
import hardcoder.dev.database.entities.features.waterTracking.WaterTrack
import hardcoder.dev.database.entities.user.User
import hardcoder.dev.database.typeConverters.InstantTypeConverter

@TypeConverters(InstantTypeConverter::class)
@Database(
    version = 1,
    entities = [
        AppPreferences::class,
        User::class,
        WaterTrack::class,
        DrinkType::class,
        PedometerTrack::class,
        MoodTrack::class,
        MoodType::class,
        MoodActivity::class,
        MoodActivityCrossRef::class,
        FoodType::class,
        FoodTrack::class,
        DiaryTag::class,
        DiaryTrack::class,
        DiaryAttachment::class,
    ],
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appPreferencesDao(): AppPreferencesDao
    abstract fun userDao(): UserDao

    abstract fun waterTrackDao(): WaterTrackDao
    abstract fun drinkTypeDao(): DrinkTypeDao

    abstract fun pedometerTrackDao(): PedometerTrackDao

    abstract fun moodTrackDao(): MoodTrackDao
    abstract fun moodTypeDao(): MoodTypeDao
    abstract fun moodActivityDao(): MoodActivityDao
    abstract fun moodWithActivityDao(): MoodWithActivityDao

    abstract fun foodTypeDao(): FoodTypeDao
    abstract fun foodTrackDao(): FoodTrackDao

    abstract fun diaryTagDao(): DiaryTagDao
    abstract fun diaryTrackDao(): DiaryTrackDao
    abstract fun diaryAttachmentDao(): DiaryAttachmentDao
}