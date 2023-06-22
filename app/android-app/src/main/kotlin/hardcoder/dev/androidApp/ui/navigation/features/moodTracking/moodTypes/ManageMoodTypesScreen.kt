package hardcoder.dev.androidApp.ui.navigation.features.moodTracking.moodTypes

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.moodTracking.moodType.ManageMoodTypes

class ManageMoodTypesScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        ManageMoodTypes(
            onGoBack = navigator::pop,
            onCreateMoodType = {
                navigator += MoodTypeCreationScreen()
            },
            onUpdateMoodType = { moodType ->
                navigator += MoodTypeUpdateScreen(moodType.id)
            }
        )
    }
}