package hardcoder.dev.androidApp.ui.features.starvation.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Start
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.ui.LocalPresentationModule
import hardcoder.dev.androidApp.ui.features.starvation.plans.StarvationPlanItem
import hardcoder.dev.entities.features.starvation.StarvationPlan
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.starvation.StarvationCreateTrackViewModel
import hardcoder.dev.uikit.IconTextButton
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType

@Composable
fun StarvationCreationTrackScreen(onGoBack: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.createStarvationCreateTrackViewModel()
    }
    val state = viewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            StarvationCreationTrackContent(
                state = state.value,
                onUpdateSelectedPlan = viewModel::updateStarvationPlan,
                onStartStarvation = {
                    viewModel.startStarvation()
                    onGoBack()
                }
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.starvationCreation_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun StarvationCreationTrackContent(
    state: StarvationCreateTrackViewModel.State,
    onUpdateSelectedPlan: (StarvationPlan) -> Unit,
    onStartStarvation: () -> Unit
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
            imageVector = Icons.Filled.Start,
            labelResId = R.string.starvationScreen_startStarvation_buttonText,
            onClick = onStartStarvation
        )
    }
}

@Composable
private fun SelectPlanSection(
    state: StarvationCreateTrackViewModel.State,
    onUpdateSelectedPlan: (StarvationPlan) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.starvationPlan_selectStarvationPlan_text),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.starvationPlan_planDifficulty_text),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.starvationPlanList) {
                StarvationPlanItem(
                    modifier = Modifier.fillMaxWidth(),
                    starvationPlan = it,
                    selectedPlan = state.selectedPlan,
                    onSelect = { onUpdateSelectedPlan(it) }
                )
            }
        }
    }
}

@Preview
@Composable
fun StarvationCreationTrackScreenPreview() {
    ScaffoldWrapper(
        content = {
            StarvationCreationTrackContent(
                onStartStarvation = {},
                onUpdateSelectedPlan = {},
                state = StarvationCreateTrackViewModel.State(
                    selectedPlan = StarvationPlan.PLAN_14_10,
                    starvationPlanList = listOf(
                        StarvationPlan.PLAN_14_10,
                        StarvationPlan.PLAN_16_8,
                        StarvationPlan.PLAN_18_6,
                        StarvationPlan.PLAN_20_4
                    )
                )
            )
        }, topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.starvationCreation_title_topBar,
                onGoBack = {}
            )
        )
    )
}