package hardcoder.dev.androidApp.ui.screens.shop

import androidx.compose.runtime.Composable
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoderdev.healther.app.android.app.R

@Composable
fun Shop(
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            ShopContent()
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.shop_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun ShopContent() {
    // TODO
}