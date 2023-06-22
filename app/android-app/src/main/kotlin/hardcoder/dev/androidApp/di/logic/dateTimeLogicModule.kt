package hardcoder.dev.androidApp.di.logic

import hardcoder.dev.logic.DateTimeProvider
import hardcoder.dev.logic.features.diary.DateRangeFilterTypeMapper
import hardcoder.dev.logic.features.diary.DateRangeFilterTypeProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dateTimeLogicModule = module {
    singleOf(::DateRangeFilterTypeProvider)

    single {
        DateRangeFilterTypeMapper(
            appPreferenceProvider = get()
        )
    }

    single {
        DateTimeProvider(updatePeriodMillis = 1000)
    }
}