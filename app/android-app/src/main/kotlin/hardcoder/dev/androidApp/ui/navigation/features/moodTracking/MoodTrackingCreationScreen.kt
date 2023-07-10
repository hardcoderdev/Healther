package hardcoder.dev.androidApp.ui.navigation.features.moodTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.navigation.features.moodTracking.moodActivities.MoodActivitiesScreen
import hardcoder.dev.androidApp.ui.navigation.features.moodTracking.moodTypes.MoodTypesScreen
import hardcoder.dev.androidApp.ui.screens.features.moodTracking.create.MoodTrackingCreation
import hardcoder.dev.presentation.features.moodTracking.MoodTrackingCreationViewModel
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel

class MoodTrackingCreationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<MoodTrackingCreationViewModel>()

        LaunchedEffectWhenExecuted(
            controller = viewModel.creationController,
            action = navigator::pop,
        )

        MoodTrackingCreation(
            viewModel = viewModel,
            onGoBack = navigator::pop,
            onManageMoodTypes = {
                navigator += MoodTypesScreen()
            },
            onManageMoodActivities = {
                navigator += MoodActivitiesScreen()
            },
        )
    }
}