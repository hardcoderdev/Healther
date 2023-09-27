package hardcoder.dev.androidApp.ui.navigation.features.fasting

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.formatters.MillisDistanceFormatter
import hardcoder.dev.androidApp.ui.screens.features.fasting.create.FastingCreation
import hardcoder.dev.androidApp.ui.screens.features.fasting.plans.FastingPlanResourcesProvider
import hardcoder.dev.presentation.features.fasting.FastingCreationViewModel
import hardcoder.dev.uikit.components.sideEffects.LaunchedEffectWhenExecuted
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class FastingCreationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<FastingCreationViewModel>()
        val fastingPlanResourcesProvider = koinInject<FastingPlanResourcesProvider>()
        val millisDistanceFormatter = koinInject<MillisDistanceFormatter>()

        LaunchedEffectWhenExecuted(
            controller = viewModel.creationController,
            action = navigator::pop,
        )

        FastingCreation(
            millisDistanceFormatter = millisDistanceFormatter,
            fastingPlanResourcesProvider = fastingPlanResourcesProvider,
            fastingPlanSelectionController = viewModel.fastingPlanSelectionController,
            customFastingHoursInputController = viewModel.customFastingHoursInputController,
            creationController = viewModel.creationController,
            onGoBack = navigator::pop,
        )
    }
}