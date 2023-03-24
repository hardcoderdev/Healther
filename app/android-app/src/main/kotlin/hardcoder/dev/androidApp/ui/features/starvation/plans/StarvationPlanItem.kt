package hardcoder.dev.androidApp.ui.features.starvation.plans

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.DateTimeFormatter
import hardcoder.dev.androidApp.ui.LocalDateTimeFormatter
import hardcoder.dev.androidApp.ui.LocalStarvationPlanResourcesProvider
import hardcoder.dev.androidApp.ui.LocalTimeUnitMapper
import hardcoder.dev.entities.features.starvation.StarvationPlan
import hardcoder.dev.healther.R
import hardcoder.dev.uikit.NumberPicker
import hardcoder.dev.uikit.card.SelectionCard
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Headline

@Composable
fun StarvationPlanItem(
    modifier: Modifier = Modifier,
    selectedPlan: StarvationPlan?,
    starvationPlan: StarvationPlan,
    onSelect: (Int?) -> Unit
) {
    val timeUnitMapper = LocalTimeUnitMapper.current
    val starvationPlanResourcesProvider = LocalStarvationPlanResourcesProvider.current
    val dateTimeFormatter = LocalDateTimeFormatter.current
    val starvationPlanResources = starvationPlanResourcesProvider.provide(starvationPlan)

    var customStarvingHours: Int? by remember {
        mutableStateOf(4)
    }

    val starvingHoursInMillis =
        timeUnitMapper.hoursToMillis(starvationPlanResources.starvingHoursCount.toLong())
    val eatingHoursInMillis =
        timeUnitMapper.hoursToMillis(starvationPlanResources.eatingHoursCount.toLong())

    SelectionCard(
        modifier = modifier,
        item = starvationPlan,
        selectedItem = selectedPlan,
        onSelect = { onSelect(customStarvingHours) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Headline(text = stringResource(id = starvationPlanResources.nameResId))
                if (starvationPlan != StarvationPlan.CUSTOM_PLAN) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Description(
                        text = stringResource(
                            id = R.string.starvationPlanItem_description_formatText,
                            formatArgs = arrayOf(
                                dateTimeFormatter.formatMillisDistance(
                                    distanceInMillis = starvingHoursInMillis,
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
                    Description(text = stringResource(id = R.string.starvationPlanItem_selectStarvingHours_text))
                    NumberPicker(
                        value = customStarvingHours ?: 4,
                        range = 4..50,
                        onValueChange = { customStarvingHours = it }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun StarvationPlanItemPreview() {
    StarvationPlanItem(
        starvationPlan = StarvationPlan.CUSTOM_PLAN,
        selectedPlan = StarvationPlan.PLAN_14_10,
        onSelect = {}
    )
}