package hardcoder.dev.androidApp.ui.navigation.features.moodTracking.moodTypes

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.moodTracking.moodType.create.MoodTypeCreation

class MoodTypeCreationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        MoodTypeCreation(
            onGoBack = navigator::pop
        )
    }
}