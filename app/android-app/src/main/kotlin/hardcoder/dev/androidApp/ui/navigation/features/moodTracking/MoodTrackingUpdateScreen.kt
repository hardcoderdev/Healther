package hardcoder.dev.androidApp.ui.navigation.features.moodTracking

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.moodTracking.update.MoodTrackingUpdate
import hardcoder.dev.androidApp.ui.navigation.features.moodTracking.moodActivities.ManageMoodActivitiesScreen
import hardcoder.dev.androidApp.ui.navigation.features.moodTracking.moodTypes.ManageMoodTypesScreen

data class MoodTrackingUpdateScreen(val moodTrackId: Int) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        MoodTrackingUpdate(
            moodTrackId = moodTrackId,
            onGoBack = navigator::pop,
            onManageMoodActivities = {
                navigator += ManageMoodActivitiesScreen()
            },
            onManageMoodTypes = {
                navigator += ManageMoodTypesScreen()
            }
        )
    }
}