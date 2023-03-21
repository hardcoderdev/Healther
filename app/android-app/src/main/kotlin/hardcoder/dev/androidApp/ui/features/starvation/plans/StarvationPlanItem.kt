@file:OptIn(ExperimentalMaterial3Api::class)

package hardcoder.dev.androidApp.ui.features.starvation.plans

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.NumberPicker
import hardcoder.dev.androidApp.ui.DateTimeFormatter
import hardcoder.dev.androidApp.ui.LocalDateTimeFormatter
import hardcoder.dev.androidApp.ui.LocalStarvationPlanResourcesProvider
import hardcoder.dev.androidApp.ui.LocalTimeUnitMapper
import hardcoder.dev.entities.features.starvation.StarvationPlan
import hardcoder.dev.healther.R

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

    val selectedBorder = if (selectedPlan == starvationPlanResources.starvationPlan) BorderStroke(
        width = 3.dp,
        color = MaterialTheme.colorScheme.primary
    ) else null

    var customStarvingHours: Int? by remember {
        mutableStateOf(4)
    }

    val starvingHoursInMillis =
        timeUnitMapper.hoursToMillis(starvationPlanResources.starvingHoursCount.toLong())
    val eatingHoursInMillis =
        timeUnitMapper.hoursToMillis(starvationPlanResources.eatingHoursCount.toLong())

    Card(
        modifier = modifier,
        onClick = { onSelect(customStarvingHours) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
        border = selectedBorder,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Text(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.inversePrimary,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    text = stringResource(id = starvationPlanResources.nameResId),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                if (starvationPlan != StarvationPlan.CUSTOM_PLAN) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        style = MaterialTheme.typography.titleMedium,
                        text = stringResource(
                            id = R.string.starvationPlan_description_formatText,
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
                    Text(
                        text = stringResource(id = R.string.starvationPlan_selectStarvingHours_text),
                        style = MaterialTheme.typography.titleMedium
                    )
                    NumberPicker(
                        value = customStarvingHours ?: 4,
                        range = 4..50,
                        dividersColor = MaterialTheme.colorScheme.primary,
                        textStyle = MaterialTheme.typography.labelLarge,
                        onValueChange = { customStarvingHours = it },
                        modifier = Modifier.fillMaxWidth()
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