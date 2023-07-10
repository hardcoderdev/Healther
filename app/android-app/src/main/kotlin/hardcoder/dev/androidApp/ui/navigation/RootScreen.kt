package hardcoder.dev.androidApp.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import hardcoder.dev.androidApp.ui.navigation.splash.SplashScreen

@Composable
fun RootScreen() {
    Navigator(
        screen = SplashScreen(),
    )
}