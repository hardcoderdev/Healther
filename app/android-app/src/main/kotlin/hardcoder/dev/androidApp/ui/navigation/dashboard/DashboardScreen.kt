package hardcoder.dev.androidApp.ui.navigation.dashboard

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.formatters.DecimalFormatter
import hardcoder.dev.androidApp.ui.formatters.MillisDistanceFormatter
import hardcoder.dev.androidApp.ui.navigation.features.diary.DiaryCreationScreen
import hardcoder.dev.androidApp.ui.navigation.features.diary.DiaryScreen
import hardcoder.dev.androidApp.ui.navigation.features.fasting.FastingCreationScreen
import hardcoder.dev.androidApp.ui.navigation.features.fasting.FastingScreen
import hardcoder.dev.androidApp.ui.navigation.features.moodTracking.MoodTrackingCreationScreen
import hardcoder.dev.androidApp.ui.navigation.features.moodTracking.MoodTrackingScreen
import hardcoder.dev.androidApp.ui.navigation.features.pedometer.PedometerScreen
import hardcoder.dev.androidApp.ui.navigation.features.waterTracking.WaterTrackingCreationScreen
import hardcoder.dev.androidApp.ui.navigation.features.waterTracking.WaterTrackingScreen
import hardcoder.dev.androidApp.ui.navigation.hero.HeroDeathScreen
import hardcoder.dev.androidApp.ui.navigation.hero.HeroInventoryScreen
import hardcoder.dev.androidApp.ui.navigation.settings.SettingsScreen
import hardcoder.dev.androidApp.ui.navigation.shop.ShopScreen
import hardcoder.dev.androidApp.ui.screens.dashboard.Dashboard
import hardcoder.dev.androidApp.ui.screens.hero.HeroImageByGenderResolver
import hardcoder.dev.presentation.dashboard.DashboardViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class DashboardScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<DashboardViewModel>()
        val heroImageByGenderResolver = koinInject<HeroImageByGenderResolver>()
        val decimalFormatter = koinInject<DecimalFormatter>()
        val millisDistanceFormatter = koinInject<MillisDistanceFormatter>()

        Dashboard(
            millisDistanceFormatter = millisDistanceFormatter,
            heroImageByGenderResolver = heroImageByGenderResolver,
            decimalFormatter = decimalFormatter,
            featureItemsLoadingController = viewModel.featuresLoadingController,
            heroItemsLoadingController = viewModel.heroLoadingController,
            pedometerToggleController = viewModel.pedometerToggleController,
            healthPointsLoadingController = viewModel.healthPointsLoadingController,
            onGoToWaterTrackingFeature = {
                navigator += WaterTrackingScreen()
            },
            onCreateWaterTrack = {
                navigator += WaterTrackingCreationScreen()
            },
            onGoToPedometerFeature = {
                navigator += PedometerScreen()
            },
            onGoToFastingFeature = {
                navigator += FastingScreen()
            },
            onStartFasting = {
                navigator += FastingCreationScreen()
            },
            onGoToMoodTrackingFeature = {
                navigator += MoodTrackingScreen()
            },
            onCreateMoodTrack = {
                navigator += MoodTrackingCreationScreen()
            },
            onGoToDiary = {
                navigator += DiaryScreen()
            },
            onCreateDiaryTrack = {
                navigator += DiaryCreationScreen()
            },
            onGoToSettings = {
                navigator += SettingsScreen()
            },
            onGoToDeathScreen = {
                navigator replaceAll HeroDeathScreen()
            },
            onGoToInventory = {
                navigator += HeroInventoryScreen()
            },
            onGoToShop = {
                navigator += ShopScreen()
            },
        )
    }
}