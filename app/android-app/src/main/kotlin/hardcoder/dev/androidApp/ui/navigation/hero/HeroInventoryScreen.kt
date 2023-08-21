package hardcoder.dev.androidApp.ui.navigation.hero

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.screens.hero.inventory.HeroInventory

class HeroInventoryScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        HeroInventory(
            onGoBack = navigator::pop,
        )
    }
}