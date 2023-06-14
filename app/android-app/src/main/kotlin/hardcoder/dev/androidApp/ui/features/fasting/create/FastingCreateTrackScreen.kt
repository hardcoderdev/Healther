package hardcoder.dev.androidApp.ui.features.fasting.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.androidApp.ui.features.fasting.plans.FastingPlanItem
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.logic.features.fasting.plan.FastingPlan
import hardcoder.dev.uikit.LaunchedEffectWhenExecuted
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.SingleCardSelectionLazyColumn
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.RequestButtonWithIcon
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Title
import hardcoderdev.healther.app.android.app.R

@Composable
fun FastingCreationTrackScreen(onGoBack: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel { presentationModule.getFastingTrackCreateViewModel() }

    LaunchedEffectWhenExecuted(controller = viewModel.creationController, action = onGoBack)

    ScaffoldWrapper(
        content = {
            FastingCreationTrackContent(
                fastingPlanSelectionController = viewModel.fastingPlanSelectionController,
                customFastingHoursInputController = viewModel.customFastingHoursInputController,
                creationController = viewModel.creationController
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.fasting_createFastingTrack_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun FastingCreationTrackContent(
    fastingPlanSelectionController: SingleSelectionController<FastingPlan>,
    customFastingHoursInputController: InputController<Int>,
    creationController: SingleRequestController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(Modifier.weight(2f)) {
            SelectPlanSection(
                fastingPlanSelectionController = fastingPlanSelectionController,
                customFastingHoursInputController = customFastingHoursInputController
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            controller = creationController,
            iconResId = R.drawable.ic_play,
            labelResId = R.string.fasting_createFastingTrack_start_buttonText
        )
    }
}

@Composable
private fun SelectPlanSection(
    fastingPlanSelectionController: SingleSelectionController<FastingPlan>,
    customFastingHoursInputController: InputController<Int>,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Title(text = stringResource(id = R.string.fasting_createFastingTrack_selectPlan_text))
        Spacer(modifier = Modifier.height(16.dp))
        Description(text = stringResource(id = R.string.fasting_createFastingTrack_planDifficulty_text))
        Spacer(modifier = Modifier.height(16.dp))
        SingleCardSelectionLazyColumn(
            modifier = Modifier.fillMaxWidth(),
            itemModifier = { Modifier.fillMaxWidth() },
            contentPadding = PaddingValues(vertical = 12.dp),
            controller = fastingPlanSelectionController,
            itemContent = { fastingPlan, _ ->
                FastingPlanItem(
                    customFastingHoursInputController = customFastingHoursInputController,
                    fastingPlan = fastingPlan
                )
            }
        )
    }
}