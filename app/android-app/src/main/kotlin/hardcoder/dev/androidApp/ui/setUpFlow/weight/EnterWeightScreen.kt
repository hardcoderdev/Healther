package hardcoder.dev.androidApp.ui.setUpFlow.weight

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.controller.InputController
import hardcoder.dev.healther.R
import hardcoder.dev.logic.hero.gender.Gender
import hardcoder.dev.uikit.NumberInput
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.ButtonWithIcon
import hardcoder.dev.uikit.text.Title

private const val MINIMUM_WEIGHT = 30
private const val MAXIMUM_WEIGHT = 400

@Composable
fun EnterWeightScreen(
    gender: Gender,
    onGoBack: () -> Unit,
    onGoForward: (Gender, Int) -> Unit,
) {
    val presentationModule = LocalPresentationModule.current

    val enterWeightViewModel = viewModel {
        presentationModule.getEnterWeightViewModel()
    }

    ScaffoldWrapper(
        content = {
            EnterWeightContent(
                onGoForward = {
                    onGoForward(
                        gender,
                        enterWeightViewModel.weightInputController.state.value.input
                    )
                },
                enterWeightViewModel.weightInputController
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.setUpFlow_enterWeight_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun EnterWeightContent(
    onGoForward: () -> Unit,
    weightInputController: InputController<Int>,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Column(Modifier.weight(2f)) {
            Title(text = stringResource(id = R.string.setUpFlow_enterWeight_enterYourWeightInKg_text))
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                NumberInput(
                    modifier = Modifier.weight(1.8f),
                    controller = weightInputController,
                    range = MINIMUM_WEIGHT..MAXIMUM_WEIGHT
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
        }
        ButtonWithIcon(
            iconResId = R.drawable.ic_done,
            labelResId = R.string.setUpFlow_enterWeight_next_buttonText,
            onClick = onGoForward
        )
    }
}