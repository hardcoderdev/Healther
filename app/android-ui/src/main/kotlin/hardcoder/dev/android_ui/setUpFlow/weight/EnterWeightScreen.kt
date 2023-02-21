package hardcoder.dev.android_ui.setUpFlow.weight

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
import hardcoder.dev.android_ui.LocalPresentationModule
import hardcoder.dev.entities.Gender
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.EnterWeightViewModel
import hardcoder.dev.uikit.IconTextButton
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType

@Composable
fun EnterWeightScreen(
    gender: Gender,
    onGoBack: () -> Unit,
    onGoForward: (Gender, Int) -> Unit,
) {
    val presentationModule = LocalPresentationModule.current

    val enterWeightViewModel = viewModel {
        presentationModule.createEnterWeightViewModel()
    }
    val state = enterWeightViewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            EnterWeightContent(
                state = state.value,
                onGoForward = { onGoForward(gender, state.value.weight) },
                onUpdateWeight = enterWeightViewModel::updateWeight
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.enterWeight_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun EnterWeightContent(
    onUpdateWeight: (Int) -> Unit,
    onGoForward: () -> Unit,
    state: EnterWeightViewModel.State
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.enterWeight_enterYourWeightInKg_text),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            NumberPicker(
                value = state.weight,
                range = MINIMUM_WEIGHT..MAXIMUM_WEIGHT,
                dividersColor = MaterialTheme.colorScheme.primary,
                textStyle = MaterialTheme.typography.titleLarge,
                onValueChange = onUpdateWeight,
                modifier = Modifier.weight(1.8f)
            )
            Spacer(modifier = Modifier.width(32.dp))
            Image(
                painter = painterResource(id = R.drawable.weight_measurement),
                contentDescription = null,
                modifier = Modifier
                    .weight(1.2f)
                    .size(60.dp)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        IconTextButton(
            iconResourceId = Icons.Default.Done,
            labelResId = R.string.enterWeight_next_button,
            onClick = onGoForward
        )
    }
}

@Preview
@Composable
fun EnterWeightScreenPreview() {
    EnterWeightScreen(
        gender = Gender.MALE,
        onGoBack = {},
        onGoForward = { _, _ -> }
    )
}

private const val MINIMUM_WEIGHT = 30
private const val MAXIMUM_WEIGHT = 400