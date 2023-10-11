package hardcoder.dev.di.logic

import hardcoder.dev.logics.features.diary.DateRangeFilterTypeProvider
import hardcoder.dev.mappers.features.diary.DateRangeFilterTypeMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val dateTimeLogicModule = module {
    singleOf(::DateRangeFilterTypeProvider)

    single {
        DateRangeFilterTypeMapper(
            dateTimeProvider = get(),
        )
    }
}