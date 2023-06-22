package hardcoder.dev.androidApp.ui.navigation.hero

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.setUpFlow.gender.SelectGender

class SelectGenderScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        SelectGender(
            onGoBack = navigator::pop,
            onGoForward = { gender ->
                navigator += EnterWeightScreen(gender)
            }
        )
    }
}