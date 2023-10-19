package hardcoder.dev.di.data

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val databaseModule = module {
    single {
        AppDatabaseFactory.create(
            androidContext(),
            name = "healther_db",
        )
    }
}