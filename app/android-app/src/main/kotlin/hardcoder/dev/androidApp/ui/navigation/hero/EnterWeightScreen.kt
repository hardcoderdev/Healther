package hardcoder.dev.androidApp.ui.navigation.hero

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.setUpFlow.weight.EnterWeight
import hardcoder.dev.logic.hero.gender.Gender

data class EnterWeightScreen(private val gender: Gender) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        EnterWeight(
            gender = gender,
            onGoBack = navigator::pop,
            onGoForward = { gender, weight ->
                navigator += EnterExerciseStressScreen(
                    gender,
                    weight
                )
            }
        )
    }
}