package hardcoder.dev.androidApp.ui.navigation.splash

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.navigation.hero.HeroCreationScreen
import hardcoder.dev.androidApp.ui.screens.welcome.Welcome

class WelcomeScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Welcome(
            onStart = {
                navigator replaceAll HeroCreationScreen()
            },
        )
    }
}