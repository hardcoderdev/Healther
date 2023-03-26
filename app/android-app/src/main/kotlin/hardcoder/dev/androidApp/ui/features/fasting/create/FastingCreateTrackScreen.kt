package hardcoder.dev.androidApp.ui.features.fasting.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.ui.LocalPresentationModule
import hardcoder.dev.androidApp.ui.features.fasting.plans.FastingPlanItem
import hardcoder.dev.entities.features.fasting.FastingPlan
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.fasting.FastingTrackCreateViewModel
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.IconTextButton
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Title

@Composable
fun FastingCreationTrackScreen(onGoBack: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getFastingTrackCreateViewModel()
    }
    val state = viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.value.creationState) {
        if (state.value.creationState is FastingTrackCreateViewModel.CreationState.Executed) {
            onGoBack()
        }
    }

    ScaffoldWrapper(
        content = {
            FastingCreationTrackContent(
                state = state.value,
                onUpdateSelectedPlan = viewModel::updateFastingPlan,
                onStartFasting = viewModel::startFasting
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.fastingCreation_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun FastingCreationTrackContent(
    state: FastingTrackCreateViewModel.State,
    onUpdateSelectedPlan: (FastingPlan, Int?) -> Unit,
    onStartFasting: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(Modifier.weight(2f)) {
            SelectPlanSection(state = state, onUpdateSelectedPlan = onUpdateSelectedPlan)
        }
        Spacer(modifier = Modifier.height(16.dp))
        IconTextButton(
            iconResId = R.drawable.ic_play,
            labelResId = R.string.fasting_start_buttonText,
            onClick = onStartFasting,
            isEnabled = state.creationAllowed
        )
    }
}

@Composable
private fun SelectPlanSection(
    state: FastingTrackCreateViewModel.State,
    onUpdateSelectedPlan: (FastingPlan, Int?) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Title(text = stringResource(id = R.string.fastingPlanItem_selectPlan_text))
        Spacer(modifier = Modifier.height(16.dp))
        Description(text = stringResource(id = R.string.fastingPlanItem_planDifficulty_text))
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.fastingPlanList) { fastingPlan ->
                FastingPlanItem(
                    modifier = Modifier.fillMaxWidth(),
                    fastingPlan = fastingPlan,
                    selectedPlan = state.selectedPlan,
                    onSelect = { onUpdateSelectedPlan(fastingPlan, it) }
                )
            }
        }
    }
}