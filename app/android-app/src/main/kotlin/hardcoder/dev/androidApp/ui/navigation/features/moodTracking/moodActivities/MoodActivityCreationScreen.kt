package hardcoder.dev.androidApp.ui.navigation.features.moodTracking.moodActivities

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.moodTracking.activity.create.MoodActivityCreation

class MoodActivityCreationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        MoodActivityCreation(
            onGoBack = navigator::pop
        )
    }
}