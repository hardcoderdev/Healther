package hardcoder.dev.mock.dataProviders.features

import hardcoder.dev.entities.features.foodTracking.FoodTrack
import hardcoder.dev.entities.features.foodTracking.FoodType
import hardcoder.dev.logics.features.foodTracking.statistic.FoodTrackingStatistics
import hardcoder.dev.mock.dataProviders.IconsMockDataProvider
import hardcoder.dev.mock.dataProviders.date.MockDateProvider
import hardcoder.dev.presentation.features.foodTracking.FastingChartData
import hardcoder.dev.presentation.features.foodTracking.FastingChartEntry

object FoodTrackingMockDataProvider {

    fun foodTypesList() = listOf(
        FoodType(
            id = 0,
            name = "Banana",
            icon = IconsMockDataProvider.icons()[0],
            isSpicy = false,
            isVegetarian = true,
            proteins = 3.3f,
            fats = 0.5f,
            carbohydrates = 0.2f,
        ),
        FoodType(
            id = 1,
            name = "Apple",
            icon = IconsMockDataProvider.icons()[1],
            isSpicy = true,
            isVegetarian = false,
            proteins = 0.2f,
            fats = 0.3f,
            carbohydrates = 1.33f,
        ),
        FoodType(
            id = 2,
            name = "Peach",
            icon = IconsMockDataProvider.icons()[2],
            isSpicy = true,
            isVegetarian = true,
            proteins = 3.43f,
            fats = 0.32f,
            carbohydrates = 2.89f,
        ),
        FoodType(
            id = 3,
            name = "Apple pie",
            icon = IconsMockDataProvider.icons()[3],
            isSpicy = false,
            isVegetarian = false,
            proteins = 0.43f,
            fats = 1.26f,
            carbohydrates = 3.00f,
        ),
    )

    fun fastingStatistics() = FoodTrackingStatistics(
        maximumDurationInHours = 4,
        minimumDurationInHours = 2,
        averageDurationInHours = 3,
    )

    fun fastingChartData() = FastingChartData(
        entriesList = listOf(
            FastingChartEntry(
                from = 0,
                to = 1,
            ),
            FastingChartEntry(
                from = 2,
                to = 3,
            ),
            FastingChartEntry(
                from = 4,
                to = 5,
            ),
            FastingChartEntry(
                from = 6,
                to = 7,
            ),
        ),
    )

    fun foodTracksList() = listOf(
        FoodTrack(
            id = 0,
            creationInstant = MockDateProvider.instant(),
            foodType = foodTypesList()[0],
            calories = 1000,
        ),
        FoodTrack(
            id = 1,
            creationInstant = MockDateProvider.instant(),
            foodType = foodTypesList()[1],
            calories = 1550,
        ),
        FoodTrack(
            id = 2,
            creationInstant = MockDateProvider.instant(),
            foodType = foodTypesList()[2],
            calories = 300,
        ),
        FoodTrack(
            id = 3,
            creationInstant = MockDateProvider.instant(),
            foodType = foodTypesList()[3],
            calories = 210,
        ),
        FoodTrack(
            id = 4,
            creationInstant = MockDateProvider.instant(),
            foodType = foodTypesList()[4],
            calories = 170,
        ),
        FoodTrack(
            id = 5,
            creationInstant = MockDateProvider.instant(),
            foodType = foodTypesList()[5],
            calories = 50,
        ),
    )
}