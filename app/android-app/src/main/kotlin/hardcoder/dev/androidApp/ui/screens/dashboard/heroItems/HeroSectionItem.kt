package hardcoder.dev.androidApp.ui.screens.dashboard.heroItems

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.formatters.DecimalFormatter
import hardcoder.dev.androidApp.ui.screens.hero.HeroImageByGenderResolver
import hardcoder.dev.logic.hero.Hero
import hardcoder.dev.mock.dataProviders.HeroMockDataProvider
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButton
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButtonConfig
import hardcoder.dev.uikit.components.card.Card
import hardcoder.dev.uikit.components.card.CardConfig
import hardcoder.dev.uikit.components.icon.Image
import hardcoder.dev.uikit.components.progressBar.LinearProgressBar
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R

@Composable
fun HeroSectionItem(
    heroImageByGenderResolver: HeroImageByGenderResolver,
    decimalFormatter: DecimalFormatter,
    hero: Hero,
    healthPointsProgress: Float,
    experiencePointsProgress: Float,
    experiencePointsToNextLevel: Float,
    onGoToInventory: () -> Unit,
    onGoToShop: () -> Unit,
) {
    Card(
        cardConfig = CardConfig.Static {
            Column(modifier = Modifier.padding(16.dp)) {
                HeaderSection(name = hero.name, level = hero.level)
                Spacer(modifier = Modifier.height(16.dp))
                HealthSection(
                    currentHealthPoints = hero.currentHealthPoints,
                    maxHealthPoints = hero.maxHealthPoints,
                    healthPointsProgress = healthPointsProgress,
                )
                Spacer(modifier = Modifier.height(16.dp))
                ExperienceSection(
                    decimalFormatter = decimalFormatter,
                    currentExperiencePoints = hero.experiencePoints,
                    experiencePointsToNextLevel = experiencePointsToNextLevel,
                    experiencePointsProgress = experiencePointsProgress,
                )
                Spacer(modifier = Modifier.height(16.dp))
                CurrencySection(
                    decimalFormatter = decimalFormatter,
                    coinsAmount = hero.coins,
                    crystalsAmount = hero.crystals,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    imageResId = heroImageByGenderResolver.resolve(hero.gender),
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(16.dp))
                ExtraActionsSection(
                    onGoToInventory = onGoToInventory,
                    onGoToShop = onGoToShop,
                )
            }
        },
    )
}

@Composable
private fun HeaderSection(
    name: String,
    level: Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Title(text = name)
        Spacer(modifier = Modifier.width(16.dp))
        Description(
            text = stringResource(
                id = R.string.dashboard_heroSection_level_format,
                formatArgs = arrayOf(level),
            ),
        )
    }
}

@Composable
private fun HealthSection(
    currentHealthPoints: Int,
    maxHealthPoints: Int,
    healthPointsProgress: Float,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Description(text = stringResource(id = R.string.dashboard_heroSection_health_points_label))
        Spacer(modifier = Modifier.width(16.dp))
        Description(
            text = stringResource(
                id = R.string.dashboard_heroSection_health_points_format,
                formatArgs = arrayOf(
                    currentHealthPoints,
                    maxHealthPoints,
                ),
            ),
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    LinearProgressBar(
        indicatorProgress = healthPointsProgress,
        indicatorColor = colorResource(id = R.color.health_points_color),
        modifier = Modifier.height(12.dp),
    )
}

@Composable
private fun ExperienceSection(
    decimalFormatter: DecimalFormatter,
    currentExperiencePoints: Float,
    experiencePointsToNextLevel: Float,
    experiencePointsProgress: Float,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Description(text = stringResource(id = R.string.dashboard_heroSection_experience_points_label))
        Spacer(modifier = Modifier.width(16.dp))
        Description(
            text = stringResource(
                id = R.string.dashboard_heroSection_experience_points_format,
                formatArgs = arrayOf(
                    decimalFormatter.roundAndFormatToString(currentExperiencePoints),
                    decimalFormatter.roundAndFormatToString(experiencePointsToNextLevel),
                ),
            ),
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    LinearProgressBar(
        indicatorProgress = experiencePointsProgress,
        indicatorColor = colorResource(id = R.color.experience_points_color),
        modifier = Modifier.height(12.dp),
    )
}

@Composable
private fun CurrencySection(
    decimalFormatter: DecimalFormatter,
    coinsAmount: Float,
    crystalsAmount: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Description(text = decimalFormatter.roundAndFormatToString(coinsAmount))
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            imageResId = R.drawable.sample_ic_coin,
            modifier = Modifier.size(24.dp),
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Description(text = crystalsAmount.toString())
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            imageResId = R.drawable.sample_ic_crystasl,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
private fun ExtraActionsSection(
    onGoToInventory: () -> Unit,
    onGoToShop: () -> Unit,
) {
    TextIconButton(
        textIconButtonConfig = TextIconButtonConfig.Outlined(
            labelResId = R.string.dashboard_heroSection_inventory_buttonText,
            iconResId = R.drawable.ic_inventory,
            onClick = onGoToInventory,
        ),
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextIconButton(
        textIconButtonConfig = TextIconButtonConfig.Outlined(
            labelResId = R.string.dashboard_heroSection_shop_buttonText,
            iconResId = R.drawable.ic_shop,
            onClick = onGoToShop,
        ),
    )
}

@HealtherScreenPhonePreviews
@Composable
private fun HeroSectionItemPreview() {
    HealtherTheme {
        HeroSectionItem(
            onGoToShop = {},
            onGoToInventory = {},
            heroImageByGenderResolver = HeroImageByGenderResolver(),
            decimalFormatter = DecimalFormatter(),
            healthPointsProgress = 0.3f,
            experiencePointsProgress = 0.5f,
            experiencePointsToNextLevel = 30f,
            hero = HeroMockDataProvider.hero(),
        )
    }
}