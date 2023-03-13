@file:OptIn(ExperimentalMaterial3Api::class)

package hardcoder.dev.androidApp.ui.features.starvation.plans

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.LocalStarvationPlanResourcesProvider
import hardcoder.dev.entities.features.starvation.StarvationPlan
import hardcoder.dev.healther.R

@Composable
fun StarvationPlanItem(
    modifier: Modifier = Modifier,
    selectedPlan: StarvationPlan,
    starvationPlan: StarvationPlan,
    onSelect: () -> Unit
) {
    val starvationPlanResourcesProvider = LocalStarvationPlanResourcesProvider.current
    val starvationPlanResources = starvationPlanResourcesProvider.provide(starvationPlan)

    val selectedBorder = if (selectedPlan == starvationPlanResources.starvationPlan) BorderStroke(
        width = 3.dp,
        color = MaterialTheme.colorScheme.primary
    ) else null

    val eatingHoursCountPlurals = pluralStringResource(
        id = R.plurals.hours,
        count = starvationPlanResources.eatingHoursCount
    )
    val starvingHoursCountPlurals = pluralStringResource(
        id = R.plurals.hours,
        count = starvationPlanResources.starvingHoursCount
    )

    val starvingStringResource = if (starvationPlan != StarvationPlan.CUSTOM_PLAN) {
        stringResource(
            id = R.string.starvationPlan_hoursForStarving_text,
            formatArgs = arrayOf(
                starvationPlanResources.starvingHoursCount,
                starvingHoursCountPlurals
            )
        )
    } else {
        stringResource(id = R.string.starvationPlan_customHoursForStarving_text)
    }

    val eatingStringResource = if (starvationPlan != StarvationPlan.CUSTOM_PLAN) {
        stringResource(
            id = R.string.starvationPlan_hoursForEating_text,
            formatArgs = arrayOf(
                starvationPlanResources.eatingHoursCount,
                eatingHoursCountPlurals
            )
        )
    } else {
        stringResource(id = R.string.starvationPlan_customHoursForEating_text)
    }

    Card(
        modifier = modifier,
        onClick = onSelect,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
        border = selectedBorder,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
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
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = starvingStringResource
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = eatingStringResource
                )
            }
        }
    }
}

@Preview
@Composable
fun StarvationPlanItemPreview() {
    StarvationPlanItem(
        starvationPlan = StarvationPlan.PLAN_18_6,
        selectedPlan = StarvationPlan.PLAN_14_10,
        onSelect = {}
    )
}