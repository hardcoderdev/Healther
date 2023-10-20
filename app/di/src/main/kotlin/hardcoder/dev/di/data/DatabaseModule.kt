package hardcoder.dev.di.data

import androidx.room.Room
import hardcoder.dev.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val databaseModule = module {
    single {
        Room.databaseBuilder(
            context = androidContext(),
            name = "healther_db",
            klass = AppDatabase::class.java,
        ).fallbackToDestructiveMigration()
            .build()
    }

    single {
        get<AppDatabase>().appPreferencesDao()
    }

    single {
        get<AppDatabase>().userDao()
    }

    single {
        get<AppDatabase>().waterTrackDao()
    }

    single {
        get<AppDatabase>().drinkTypeDao()
    }

    single {
        get<AppDatabase>().pedometerTrackDao()
    }

    single {
        get<AppDatabase>().moodTrackDao()
    }

    single {
        get<AppDatabase>().moodWithActivityDao()
    }

    single {
        get<AppDatabase>().moodTypeDao()
    }

    single {
        get<AppDatabase>().moodActivityDao()
    }

    single {
        get<AppDatabase>().foodTrackDao()
    }

    single {
        get<AppDatabase>().foodTypeDao()
    }

    single {
        get<AppDatabase>().diaryTrackDao()
    }

    single {
        get<AppDatabase>().diaryTagDao()
    }

    single {
        get<AppDatabase>().diaryAttachmentDao()
    }
}