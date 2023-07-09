package hardcoder.dev.androidApp.di.logic.foundation

import hardcoder.dev.datetime.DateTimeProvider
import org.koin.dsl.module

val dateTimeModule = module {
    single {
        DateTimeProvider(
            dispatchers = get(),
        )
    }
}