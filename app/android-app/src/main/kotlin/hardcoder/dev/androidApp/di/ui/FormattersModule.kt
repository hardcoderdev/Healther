package hardcoder.dev.androidApp.di.ui

import hardcoder.dev.formatters.DateTimeFormatter
import hardcoder.dev.formatters.DecimalFormatter
import hardcoder.dev.formatters.LiquidFormatter
import hardcoder.dev.formatters.MillisDistanceFormatter
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val formattersModule = module {
    singleOf(::DecimalFormatter)

    single {
        DateTimeFormatter(
            context = androidContext(),
        )
    }

    single {
        MillisDistanceFormatter(
            context = androidContext(),
            defaultAccuracy = MillisDistanceFormatter.Accuracy.MINUTES,
        )
    }

    single {
        LiquidFormatter(
            context = androidContext(),
            defaultAccuracy = LiquidFormatter.Accuracy.MILLILITERS,
        )
    }
}