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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.healther.R
import hardcoder.dev.logic.hero.gender.Gender
import hardcoder.dev.presentation.setUpFlow.EnterExerciseStressTimeViewModel
import hardcoder.dev.uikit.NumberPicker
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.ButtonWithIcon
import hardcoder.dev.uikit.text.Title

@Composable
fun EnterExerciseStressScreen(
    gender: Gender,
    weight: Int,
    exerciseStressTime: Int,
    onGoBack: () -> Unit,
    onGoForward: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current

    val enterExerciseStressTimeViewModel = viewModel {
        presentationModule.getEnterExerciseStressTimeViewModel()
    }
    val heroCreateViewModel = viewModel {
        presentationModule.getHeroCreateViewModel()
    }
    val state = enterExerciseStressTimeViewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            EnterExerciseStressContent(
                state = state.value,
                onGoForward = {
                    heroCreateViewModel.createUserHero(gender, weight, exerciseStressTime)
                    onGoForward()
                },
                onUpdateExerciseStressTime = enterExerciseStressTimeViewModel::updateExerciseStressTime
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
    state: EnterExerciseStressTimeViewModel.State,
    onUpdateExerciseStressTime: (Int) -> Unit,
    onGoForward: () -> Unit
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
                NumberPicker(
                    value = state.exerciseStressTime,
                    range = MINIMUM_HOURS..MAXIMUM_HOURS,
                    onValueChange = { onUpdateExerciseStressTime(it) },
                    modifier = Modifier.weight(1.8f)
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
        ButtonWithIcon(
            iconResId = R.drawable.ic_done,
            labelResId = R.string.setUpFlow_enterExerciseStress_next_buttonText,
            onClick = onGoForward
        )
    }
}


@Preview
@Composable
fun EnterExerciseStressScreenPreview() {
    EnterExerciseStressScreen(
        onGoBack = {},
        onGoForward = {},
        gender = Gender.MALE,
        weight = 60,
        exerciseStressTime = 2
    )
}

private const val MINIMUM_HOURS = 0
private const val MAXIMUM_HOURS = 24