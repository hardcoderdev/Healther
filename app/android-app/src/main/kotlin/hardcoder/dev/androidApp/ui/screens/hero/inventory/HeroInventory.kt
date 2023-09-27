package hardcoder.dev.androidApp.ui.screens.hero.inventory

import androidx.compose.runtime.Composable
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R

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

@HealtherScreenPhonePreviews
@Composable
private fun HeroInventoryPreview() {
    HealtherTheme {
        HeroInventory(
            onGoBack = {}
        )
    }
}