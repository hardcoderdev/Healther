package hardcoder.dev.androidApp.ui.screens.features.fasting.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.screens.features.fasting.plans.FastingPlanItem
import hardcoder.dev.androidApp.ui.screens.features.fasting.plans.FastingPlanResourcesProvider
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.logic.features.fasting.plan.FastingPlan
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.list.card.SingleCardSelectionLazyColumn
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R

@Composable
fun FastingCreation(
    millisDistanceFormatter: hardcoder.dev.formatters.MillisDistanceFormatter,
    fastingPlanResourcesProvider: FastingPlanResourcesProvider,
    fastingPlanSelectionController: SingleSelectionController<FastingPlan>,
    customFastingHoursInputController: InputController<Int>,
    creationController: RequestController,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            FastingCreationContent(
                millisDistanceFormatter = millisDistanceFormatter,
                fastingPlanResourcesProvider = fastingPlanResourcesProvider,
                fastingPlanSelectionController = fastingPlanSelectionController,
                customFastingHoursInputController = customFastingHoursInputController,
                creationController = creationController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.fasting_creation_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun FastingCreationContent(
    millisDistanceFormatter: hardcoder.dev.formatters.MillisDistanceFormatter,
    fastingPlanResourcesProvider: FastingPlanResourcesProvider,
    fastingPlanSelectionController: SingleSelectionController<FastingPlan>,
    customFastingHoursInputController: InputController<Int>,
    creationController: RequestController,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(Modifier.weight(2f)) {
            SelectPlanSection(
                fastingPlanSelectionController = fastingPlanSelectionController,
                millisDistanceFormatter = millisDistanceFormatter,
                fastingPlanResourcesProvider = fastingPlanResourcesProvider,
                customFastingHoursInputController = customFastingHoursInputController,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            requestButtonConfig = RequestButtonConfig.Filled(
                controller = creationController,
                iconResId = R.drawable.ic_play,
                labelResId = R.string.fasting_creation_start_buttonText,
            ),
        )
    }
}

@Composable
private fun SelectPlanSection(
    millisDistanceFormatter: hardcoder.dev.formatters.MillisDistanceFormatter,
    fastingPlanResourcesProvider: FastingPlanResourcesProvider,
    fastingPlanSelectionController: SingleSelectionController<FastingPlan>,
    customFastingHoursInputController: InputController<Int>,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Title(text = stringResource(id = R.string.fasting_creation_selectPlan_text))
        Spacer(modifier = Modifier.height(16.dp))
        Description(text = stringResource(id = R.string.fasting_creation_planDifficulty_text))
        Spacer(modifier = Modifier.height(16.dp))
        SingleCardSelectionLazyColumn(
            modifier = Modifier.fillMaxWidth(),
            itemModifier = { Modifier.fillMaxWidth() },
            contentPadding = PaddingValues(vertical = 12.dp),
            controller = fastingPlanSelectionController,
            itemContent = { fastingPlan, _ ->
                FastingPlanItem(
                    millisDistanceFormatter = millisDistanceFormatter,
                    fastingPlanResourcesProvider = fastingPlanResourcesProvider,
                    customFastingHoursInputController = customFastingHoursInputController,
                    fastingPlan = fastingPlan,
                )
            },
        )
    }
}

@HealtherScreenPhonePreviews
@Composable
private fun FastingCreationPreview() {
    HealtherTheme {
        FastingCreation(
            onGoBack = {},
            fastingPlanResourcesProvider = FastingPlanResourcesProvider(),
            millisDistanceFormatter = hardcoder.dev.formatters.MillisDistanceFormatter(
                context = LocalContext.current,
                defaultAccuracy = hardcoder.dev.formatters.MillisDistanceFormatter.Accuracy.DAYS,
            ),
            creationController = MockControllersProvider.requestController(),
            customFastingHoursInputController = MockControllersProvider.inputController(0),
            fastingPlanSelectionController = MockControllersProvider.singleSelectionController(
                dataList = FastingPlan.entries,
            ),
        )
    }
}