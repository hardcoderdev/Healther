package hardcoder.dev.androidApp.ui.screens.features.fasting.statistics

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.screens.features.fasting.plans.FastingPlanResourcesProvider
import hardcoder.dev.logic.features.fasting.statistic.FastingStatistic
import hardcoder.dev.uikit.components.statistic.Statistics
import hardcoder.dev.uikit.components.text.Title
import hardcoderdev.healther.app.android.app.R
import org.koin.compose.koinInject

@Composable
fun FastingStatisticSection(statistic: FastingStatistic) {
    val fastingStatisticResolver = koinInject<FastingStatisticResolver>()
    val fastingPlanResourcesProvider = koinInject<FastingPlanResourcesProvider>()

    Title(text = stringResource(id = R.string.fasting_statistic_text))
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