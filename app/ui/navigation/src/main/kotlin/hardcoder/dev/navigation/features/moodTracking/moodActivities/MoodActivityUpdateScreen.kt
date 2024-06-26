package hardcoder.dev.navigation.features.moodTracking.moodActivities

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.presentation.features.moodTracking.activity.MoodActivityUpdateViewModel
import hardcoder.dev.screens.features.moodTracking.activity.update.MoodActivityUpdate
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.core.parameter.parametersOf

data class MoodActivityUpdateScreen(val moodActivityId: Int) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<MoodActivityUpdateViewModel> {
            parametersOf(moodActivityId)
        }

        LaunchedEffectWhenExecuted(
            controller = viewModel.updateController,
            action = navigator::pop,
        )
        LaunchedEffectWhenExecuted(
            controller = viewModel.deleteController,
            action = navigator::pop,
        )

        MoodActivityUpdate(
            activityNameController = viewModel.activityNameController,
            iconSingleSelectionController = viewModel.iconSingleSelectionController,
            updateController = viewModel.updateController,
            deleteController = viewModel.deleteController,
            onGoBack = navigator::pop,
        )
    }
}