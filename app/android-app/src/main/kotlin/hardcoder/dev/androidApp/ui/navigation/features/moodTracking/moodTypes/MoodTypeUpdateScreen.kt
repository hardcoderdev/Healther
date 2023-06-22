package hardcoder.dev.androidApp.ui.navigation.features.moodTracking.moodTypes

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.moodTracking.moodType.update.MoodTypeUpdate

data class MoodTypeUpdateScreen(val moodTypeId: Int) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        MoodTypeUpdate(
            moodTypeId = moodTypeId,
            onGoBack = navigator::pop
        )
    }
}
