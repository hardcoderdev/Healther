package hardcoder.dev.androidApp.di.ui

import hardcoder.dev.formatters.DecimalFormatter
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val formattersModule = module {
    singleOf(::DecimalFormatter)

    single {
        hardcoder.dev.formatters.DateTimeFormatter(
            context = androidContext(),
        )
    }

    single {
        hardcoder.dev.formatters.MillisDistanceFormatter(
            context = androidContext(),
            defaultAccuracy = hardcoder.dev.formatters.MillisDistanceFormatter.Accuracy.MINUTES,
        )
    }

    single {
        hardcoder.dev.formatters.LiquidFormatter(
            context = androidContext(),
            defaultAccuracy = hardcoder.dev.formatters.LiquidFormatter.Accuracy.MILLILITERS,
        )
    }
}