package hardcoder.dev.androidApp.di.ui

import hardcoder.dev.androidApp.ui.formatters.DateTimeFormatter
import hardcoder.dev.androidApp.ui.formatters.DecimalFormatter
import hardcoder.dev.androidApp.ui.formatters.LiquidFormatter
import hardcoder.dev.androidApp.ui.formatters.MillisDistanceFormatter
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