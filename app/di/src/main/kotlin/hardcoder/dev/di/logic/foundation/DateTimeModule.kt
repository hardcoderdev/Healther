package hardcoder.dev.di.logic.foundation

import hardcoder.dev.datetime.DateTimeProvider
import org.koin.dsl.module

internal val dateTimeModule = module {
    single {
        DateTimeProvider(
            dispatchers = get(),
        )
    }
}