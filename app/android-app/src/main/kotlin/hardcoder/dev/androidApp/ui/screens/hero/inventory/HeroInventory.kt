package hardcoder.dev.androidApp.ui.screens.hero.inventory

import androidx.compose.runtime.Composable
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoderdev.healther.app.android.app.R

@Composable
fun HeroInventory(
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            HeroInventoryContent()
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.hero_inventory_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun HeroInventoryContent() {
    // TODO
}