package hardcoder.dev.androidApp.ui.screens.features.fasting.plans

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.formatters.MillisDistanceFormatter
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.logic.features.fasting.plan.FastingPlan
import hardcoder.dev.uikit.components.picker.NumberInput
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Headline
import hardcoderdev.healther.app.android.app.R
import kotlin.time.Duration.Companion.hours
import org.koin.compose.koinInject

@Composable
fun FastingPlanItem(
    customFastingHoursInputController: InputController<Int>,
    modifier: Modifier = Modifier,
    fastingPlan: FastingPlan,
) {
    val fastingPlanResourcesProvider = koinInject<FastingPlanResourcesProvider>()
    val millisDistanceFormatter = koinInject<MillisDistanceFormatter>()
    val fastingPlanResources = fastingPlanResourcesProvider.provide(fastingPlan)

    val fastingHoursInMillis = fastingPlanResources.fastingHoursCount.hours.inWholeMilliseconds
    val eatingHoursInMillis = fastingPlanResources.eatingHoursCount.hours.inWholeMilliseconds

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column {
            Headline(text = stringResource(id = fastingPlanResources.nameResId))
            if (fastingPlan != FastingPlan.CUSTOM_PLAN) {
                Spacer(modifier = Modifier.height(32.dp))
                Description(
                    text = stringResource(
                        id = R.string.fasting_itemPlan_description_formatText,
                        formatArgs = arrayOf(
                            millisDistanceFormatter.formatMillisDistance(
                                distanceInMillis = fastingHoursInMillis,
                                accuracy = MillisDistanceFormatter.Accuracy.HOURS,
                                usePlurals = true,
                            ),
                            millisDistanceFormatter.formatMillisDistance(
                                distanceInMillis = eatingHoursInMillis,
                                accuracy = MillisDistanceFormatter.Accuracy.HOURS,
                                usePlurals = true,
                            ),
                        ),
                    ),
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                Description(text = stringResource(id = R.string.fasting_itemPlan_selectFastingHours_text))
                NumberInput(
                    controller = customFastingHoursInputController,
                    modifier = Modifier.fillMaxWidth(),
                    range = 4..50,
                )
            }
        }
    }
}