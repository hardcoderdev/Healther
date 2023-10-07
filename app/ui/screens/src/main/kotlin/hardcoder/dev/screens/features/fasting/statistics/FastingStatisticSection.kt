package hardcoder.dev.screens.features.fasting.statistics

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.entities.features.fasting.FastingStatistics
import hardcoder.dev.resolvers.features.fasting.FastingStatisticResolver
import hardcoder.dev.resources.features.fasting.FastingPlanResourcesProvider
import hardcoder.dev.uikit.components.statistics.Statistics
import hardcoder.dev.uikit.components.text.Title
import hardcoderdev.healther.app.ui.resources.R

@Composable
fun FastingStatisticSection(
    statistic: FastingStatistics,
    fastingStatisticResolver: FastingStatisticResolver,
    fastingPlanResourcesProvider: FastingPlanResourcesProvider,
) {
    Title(text = stringResource(id = R.string.analytics_generalStatistics_text))
    Spacer(modifier = Modifier.height(24.dp))
    Statistics(
        modifier = Modifier.fillMaxWidth(),
        statistics = fastingStatisticResolver.resolve(
            statistic,
            statistic.favouritePlan?.let {
                fastingPlanResourcesProvider.provide(it).nameResId
            },
        ),
    )
}