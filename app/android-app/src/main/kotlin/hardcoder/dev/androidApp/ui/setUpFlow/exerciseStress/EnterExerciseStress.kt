package hardcoder.dev.androidApp.ui.setUpFlow.exerciseStress

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.logic.hero.gender.Gender
import hardcoder.dev.presentation.setUpFlow.EnterExerciseStressTimeViewModel
import hardcoder.dev.presentation.setUpFlow.HeroCreateViewModel
import hardcoder.dev.uikit.LaunchedEffectWhenExecuted
import hardcoder.dev.uikit.NumberInput
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.RequestButtonWithIcon
import hardcoder.dev.uikit.text.Title
import hardcoderdev.healther.app.android.app.R
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

private const val MINIMUM_HOURS = 0
private const val MAXIMUM_HOURS = 24

@Composable
fun EnterExerciseStress(
    gender: Gender,
    weight: Int,
    onGoBack: () -> Unit,
    onGoForward: () -> Unit
) {
    val enterExerciseStressTimeViewModel = koinViewModel<EnterExerciseStressTimeViewModel>()
    val exerciseStressTimeInputState = enterExerciseStressTimeViewModel.exerciseStressTimeInputController.state.collectAsState()
    val heroCreateViewModel = koinViewModel<HeroCreateViewModel> {
        parametersOf(
            gender,
            weight,
            exerciseStressTimeInputState.value.input
        )
    }

    LaunchedEffectWhenExecuted(heroCreateViewModel.creationController, onGoForward)

    ScaffoldWrapper(
        content = {
            EnterExerciseStressContent(
                heroCreateViewModel.creationController,
                enterExerciseStressTimeViewModel.exerciseStressTimeInputController
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.setUpFlow_enterExerciseStress_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun EnterExerciseStressContent(
    heroCreationController: SingleRequestController,
    exerciseStressTimeInputController: InputController<Int>
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Column(Modifier.weight(2f)) {
            Title(text = stringResource(id = R.string.setUpFlow_enterExerciseStress_enterExerciseStressTime_text))
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                NumberInput(
                    modifier = Modifier.weight(1.8f),
                    controller = exerciseStressTimeInputController,
                    range = MINIMUM_HOURS..MAXIMUM_HOURS
                )
                Spacer(modifier = Modifier.width(32.dp))
                Image(
                    painter = painterResource(id = R.drawable.exercise_stress_time),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1.2f)
                        .size(60.dp)
                )
            }
        }
        RequestButtonWithIcon(
            controller = heroCreationController,
            iconResId = R.drawable.ic_done,
            labelResId = R.string.setUpFlow_enterExerciseStress_next_buttonText
        )
    }
}