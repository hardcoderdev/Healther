package hardcoder.dev.androidApp.ui.navigation.features.moodTracking.moodTypes

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypesViewModel
import hardcoder.dev.screens.features.moodTracking.moodType.MoodTypes
import org.koin.androidx.compose.koinViewModel

class MoodTypesScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<MoodTypesViewModel>()

        MoodTypes(
            moodTypesLoadingController = viewModel.moodTypesLoadingController,
            onGoBack = navigator::pop,
            onCreateMoodType = {
                navigator += MoodTypeCreationScreen()
            },
            onUpdateMoodType = { moodTypeId ->
                navigator += MoodTypeUpdateScreen(moodTypeId)
            },
        )
    }
}