package hardcoder.dev.healther.ui.screens.setUpFlow.exerciseStress

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chargemap.compose.numberpicker.NumberPicker
import hardcoder.dev.healther.R
import hardcoder.dev.healther.entities.Gender
import hardcoder.dev.healther.ui.base.LocalPresentationModule
import hardcoder.dev.healther.ui.base.composables.IconTextButton
import hardcoder.dev.healther.ui.base.composables.ScaffoldWrapper
import hardcoder.dev.healther.ui.base.composables.TopBarConfig
import hardcoder.dev.healther.ui.base.composables.TopBarType

@Composable
fun EnterExerciseStressScreen(
    gender: Gender,
    weight: Int,
    exerciseStressTime: Int,
    onGoBack: () -> Unit,
    onGoForward: (Gender, Int, Int) -> Unit
) {
    val presentationModule = LocalPresentationModule.current

    val enterExerciseStressTimeViewModel = viewModel {
        presentationModule.createEnterExerciseStressTimeViewModel()
    }
    val state = enterExerciseStressTimeViewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            EnterExerciseStressContent(
                state = state.value,
                onGoForward = {
                    onGoForward(gender, weight, exerciseStressTime)
                },
                onUpdateExerciseStressTime = enterExerciseStressTimeViewModel::updateExerciseStressTime
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.enterExerciseStress_title_topBar,
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
        Text(
            text = stringResource(id = R.string.enterExerciseStress_enterExerciseStressTime_text),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            NumberPicker(
                value = state.exerciseStressTime,
                range = MINIMUM_HOURS..MAXIMUM_HOURS,
                dividersColor = MaterialTheme.colorScheme.primary,
                textStyle = MaterialTheme.typography.titleLarge,
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
        Spacer(modifier = Modifier.height(32.dp))
        IconTextButton(
            iconResourceId = Icons.Default.Done,
            labelResId = R.string.enterExerciseStress_next_button,
            onClick = onGoForward
        )
    }
}


@Preview
@Composable
fun EnterExerciseStressScreenPreview() {
    EnterExerciseStressScreen(
        onGoBack = {},
        onGoForward = { gender, weight, exerciseStressTime -> },
        gender = Gender.MALE,
        weight = 60,
        exerciseStressTime = 2
    )
}

private const val MINIMUM_HOURS = 0
private const val MAXIMUM_HOURS = 24