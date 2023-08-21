package hardcoder.dev.androidApp.ui.navigation.hero

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import hardcoder.dev.androidApp.ui.screens.hero.death.HeroDeath

class HeroDeathScreen : Screen {

    @Composable
    override fun Content() {
        HeroDeath()
    }
}