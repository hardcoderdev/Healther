package hardcoder.dev.androidApp.ui.features.fasting.plans

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
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.LocalDateTimeFormatter
import hardcoder.dev.androidApp.ui.LocalFastingPlanResourcesProvider
import hardcoder.dev.androidApp.ui.formatters.DateTimeFormatter
import hardcoder.dev.entities.features.fasting.FastingPlan
import hardcoder.dev.healther.R
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.NumberPicker
import hardcoder.dev.uikit.card.Card
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Headline
import kotlin.time.Duration.Companion.hours

@Composable
fun FastingPlanItem(
    modifier: Modifier = Modifier,
    selectedPlan: FastingPlan?,
    fastingPlan: FastingPlan,
    onSelect: (Int?) -> Unit
) {
    val fastingPlanResourcesProvider = LocalFastingPlanResourcesProvider.current
    val dateTimeFormatter = LocalDateTimeFormatter.current
    val fastingPlanResources = fastingPlanResourcesProvider.provide(fastingPlan)

    var customFastingHours: Int? by remember {
        mutableStateOf(4)
    }

    val fastingHoursInMillis = fastingPlanResources.fastingHoursCount.hours.inWholeMilliseconds
    val eatingHoursInMillis = fastingPlanResources.eatingHoursCount.hours.inWholeMilliseconds

    Card(
        interactionType = InteractionType.SELECTION,
        modifier = modifier,
        item = fastingPlan,
        selectedItem = selectedPlan,
        onClick = { onSelect(customFastingHours) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Headline(text = stringResource(id = fastingPlanResources.nameResId))
                if (fastingPlan != FastingPlan.CUSTOM_PLAN) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Description(
                        text = stringResource(
                            id = R.string.fastingPlanItem_description_formatText,
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
                    Description(text = stringResource(id = R.string.fastingPlanItem_selectFastingHours_text))
                    NumberPicker(
                        modifier = Modifier.fillMaxWidth(),
                        value = customFastingHours ?: 4,
                        range = 4..50,
                        onValueChange = { customFastingHours = it }
                    )
                }
            }
        }
    }
}