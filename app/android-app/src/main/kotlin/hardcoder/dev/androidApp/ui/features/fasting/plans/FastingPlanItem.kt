package hardcoder.dev.androidApp.ui.features.fasting.plans

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.di.LocalUIModule
import hardcoder.dev.androidApp.ui.formatters.DateTimeFormatter
import hardcoder.dev.controller.InputController
import hardcoder.dev.logic.features.fasting.plan.FastingPlan
import hardcoder.dev.uikit.NumberInput
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Headline
import hardcoderdev.healther.app.android.app.R
import kotlin.time.Duration.Companion.hours

@Composable
fun FastingPlanItem(
    customFastingHoursInputController: InputController<Int>,
    modifier: Modifier = Modifier,
    fastingPlan: FastingPlan
) {
    val uiModule = LocalUIModule.current

    val fastingPlanResourcesProvider = uiModule.fastingPlanResourcesProvider
    val dateTimeFormatter = uiModule.dateTimeFormatter
    val fastingPlanResources = fastingPlanResourcesProvider.provide(fastingPlan)

    val fastingHoursInMillis = fastingPlanResources.fastingHoursCount.hours.inWholeMilliseconds
    val eatingHoursInMillis = fastingPlanResources.eatingHoursCount.hours.inWholeMilliseconds

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column {
            Headline(text = stringResource(id = fastingPlanResources.nameResId))
            if (fastingPlan != FastingPlan.CUSTOM_PLAN) {
                Spacer(modifier = Modifier.height(32.dp))
                Description(
                    text = stringResource(
                        id = R.string.fasting_itemPlan_description_formatText,
                        formatArgs = arrayOf(
                            dateTimeFormatter.formatMillisDistance(
                                distanceInMillis = fastingHoursInMillis,
                                accuracy = DateTimeFormatter.Accuracy.HOURS,
                                usePlurals = true
                            ), dateTimeFormatter.formatMillisDistance(
                                distanceInMillis = eatingHoursInMillis,
                                accuracy = DateTimeFormatter.Accuracy.HOURS,
                                usePlurals = true
                            )
                        )
                    )
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                Description(text = stringResource(id = R.string.fasting_itemPlan_selectFastingHours_text))
                NumberInput(
                    controller = customFastingHoursInputController,
                    modifier = Modifier.fillMaxWidth(),
                    range = 4..50
                )
            }
        }
    }
}