package hardcoder.dev.androidApp.ui.navigation.hero

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.navigation.dashboard.DashboardScreen
import hardcoder.dev.androidApp.ui.setUpFlow.exerciseStress.EnterExerciseStress
import hardcoder.dev.logic.hero.gender.Gender

class EnterExerciseStressScreen(
    private val gender: Gender,
    private val weight: Int
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        EnterExerciseStress(
            gender = gender,
            weight = weight,
            onGoBack = navigator::pop,
            onGoForward = {
                navigator replaceAll DashboardScreen()
            }
        )
    }
}