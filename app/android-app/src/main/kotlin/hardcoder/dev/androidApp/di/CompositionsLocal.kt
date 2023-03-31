package hardcoder.dev.androidApp.di

import androidx.compose.runtime.compositionLocalOf

val LocalPresentationModule = compositionLocalOf<PresentationModule> {
    error("LocalPresentationModule not provided")
}

val LocalLogicModule = compositionLocalOf<LogicModule> {
    error("LocalLogicModule not provided")
}

val LocalUIModule = compositionLocalOf<UIModule> {
    error("LocalUIModule not provided")
}