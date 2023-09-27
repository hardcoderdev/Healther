package hardcoder.dev.mock.dataProviders.features

import hardcoder.dev.logic.features.pedometer.statistic.PedometerStatistic
import hardcoder.dev.mock.dataProviders.date.MockDateProvider
import hardcoder.dev.presentation.features.pedometer.PedometerChartData
import hardcoder.dev.presentation.features.pedometer.PedometerChartEntry

object PedometerMockDataProvider {

    fun pedometerStatistics() = PedometerStatistic(
        totalSteps = 14000,
        totalKilometers = 14f,
        totalDuration = MockDateProvider.duration(),
        totalCalories = 1000f,
    )

    fun pedometerChartData() = PedometerChartData(
        entriesList = listOf(
            PedometerChartEntry(
                from = 0,
                to = 1,
            ),
            PedometerChartEntry(
                from = 2,
                to = 3,
            ),
            PedometerChartEntry(
                from = 4,
                to = 5,
            ),
            PedometerChartEntry(
                from = 6,
                to = 7,
            ),
        ),
    )
}