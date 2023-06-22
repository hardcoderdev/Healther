package hardcoder.dev.androidApp.di.data

import hardcoder.dev.database.AppDatabaseFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        AppDatabaseFactory.create(
            androidContext(),
            name = "healther_db"
        )
    }
}