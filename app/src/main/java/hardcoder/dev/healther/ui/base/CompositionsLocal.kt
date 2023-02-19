package hardcoder.dev.healther.ui.base

import androidx.compose.runtime.compositionLocalOf
import hardcoder.dev.healther.di.PresentationModule

val LocalPresentationModule = compositionLocalOf<PresentationModule> {
    error("LocalPresentationModule not provided")
}