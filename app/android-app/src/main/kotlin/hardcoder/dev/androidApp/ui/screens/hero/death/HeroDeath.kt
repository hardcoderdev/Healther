package hardcoder.dev.androidApp.ui.screens.hero.death

import androidx.compose.runtime.Composable
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R

@Composable
fun HeroDeath() {
    ScaffoldWrapper(
        content = {
            HeroDeathContent()
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TitleTopBar(
                titleResId = R.string.hero_death_title_topBar,
            ),
        ),
    )
}

@Composable
private fun HeroDeathContent() {
}

@HealtherScreenPhonePreviews
@Composable
private fun HeroDeathPreview() {
    HealtherTheme {
        HeroDeath()
    }
}