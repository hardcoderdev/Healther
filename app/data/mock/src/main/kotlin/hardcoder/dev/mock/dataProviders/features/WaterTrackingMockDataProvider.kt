package hardcoder.dev.mock.dataProviders.features

import android.content.Context
import hardcoder.dev.icons.IconImpl
import hardcoder.dev.logic.features.waterTracking.MillilitersDrunkToDailyRate
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkType
import hardcoder.dev.logic.features.waterTracking.statistic.WaterTrackingStatistic
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingChartData
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingChartEntry
import hardcoder.dev.presentation.features.waterTracking.WaterTrackingItem
import hardcoderdev.healther.app.resources.R

object WaterTrackingMockDataProvider {

    fun waterTrackingItemsList(context: Context): List<WaterTrackingItem> {
        val drinkTypesList = provideDrinkTypesList(context)

        return listOf(
            WaterTrackingItem(
                id = 0,
                drinkType = drinkTypesList[0],
                millilitersCount = 1200,
                resolvedMillilitersCount = 1000,
                timeInMillis = System.currentTimeMillis(),
            ),
            WaterTrackingItem(
                id = 1,
                drinkType = drinkTypesList[1],
                millilitersCount = 800,
                resolvedMillilitersCount = 600,
                timeInMillis = System.currentTimeMillis(),
            ),
            WaterTrackingItem(
                id = 2,
                drinkType = drinkTypesList[2],
                millilitersCount = 200,
                resolvedMillilitersCount = 100,
                timeInMillis = System.currentTimeMillis(),
            ),
            WaterTrackingItem(
                id = 3,
                drinkType = drinkTypesList[3],
                millilitersCount = 100,
                resolvedMillilitersCount = 50,
                timeInMillis = System.currentTimeMillis(),
            ),
            WaterTrackingItem(
                id = 4,
                drinkType = drinkTypesList[4],
                millilitersCount = 150,
                resolvedMillilitersCount = 100,
                timeInMillis = System.currentTimeMillis(),
            ),
            WaterTrackingItem(
                id = 5,
                drinkType = drinkTypesList[5],
                millilitersCount = 700,
                resolvedMillilitersCount = 350,
                timeInMillis = System.currentTimeMillis(),
            ),
            WaterTrackingItem(
                id = 6,
                drinkType = drinkTypesList[6],
                millilitersCount = 50,
                resolvedMillilitersCount = 50,
                timeInMillis = System.currentTimeMillis(),
            ),
            WaterTrackingItem(
                id = 7,
                drinkType = drinkTypesList[7],
                millilitersCount = 200,
                resolvedMillilitersCount = 200,
                timeInMillis = System.currentTimeMillis(),
            ),
        )
    }

    fun provideDrinkTypesList(context: Context): List<DrinkType> {
        return listOf(
            DrinkType(
                id = 0,
                name = context.getString(R.string.predefined_drinkType_name_water),
                icon = IconImpl(id = 0, resourceId = R.drawable.ic_apps),
                hydrationIndexPercentage = 80,
            ),
            DrinkType(
                id = 1,
                name = context.getString(R.string.predefined_drinkType_name_tea),
                icon = IconImpl(id = 1, resourceId = R.drawable.ic_create),
                hydrationIndexPercentage = 70,
            ),
            DrinkType(
                id = 2,
                name = context.getString(R.string.predefined_drinkType_name_coffee),
                icon = IconImpl(id = 1, resourceId = R.drawable.ic_airplane),
                hydrationIndexPercentage = 50,
            ),
            DrinkType(
                id = 3,
                name = context.getString(R.string.predefined_drinkType_name_beer),
                icon = IconImpl(id = 1, resourceId = R.drawable.ic_play),
                hydrationIndexPercentage = 90,
            ),
            DrinkType(
                id = 4,
                name = context.getString(R.string.predefined_drinkType_name_milk),
                icon = IconImpl(id = 1, resourceId = R.drawable.ic_baseball),
                hydrationIndexPercentage = 20,
            ),
            DrinkType(
                id = 5,
                name = context.getString(R.string.predefined_drinkType_name_juice),
                icon = IconImpl(id = 1, resourceId = R.drawable.ic_analytics),
                hydrationIndexPercentage = 65,
            ),
            DrinkType(
                id = 6,
                name = context.getString(R.string.predefined_drinkType_name_soda),
                icon = IconImpl(id = 1, resourceId = R.drawable.ic_celebration),
                hydrationIndexPercentage = 20,
            ),
            DrinkType(
                id = 7,
                name = context.getString(R.string.predefined_drinkType_name_soup),
                icon = IconImpl(id = 1, resourceId = R.drawable.ic_coffee),
                hydrationIndexPercentage = 50,
            ),
        )
    }

    fun millilitersDrunkToDailyRate() = MillilitersDrunkToDailyRate(
        millilitersDrunkCount = 2000,
        dailyWaterIntake = 3000,
    )

    fun waterTrackingStatistics(context: Context) = WaterTrackingStatistic(
        totalMilliliters = 12_000,
        averageHydrationIndex = 50,
        averageWaterIntakes = 3,
        favouriteDrinkTypeId = DrinkType(
            id = 0,
            name = context.getString(R.string.predefined_drinkType_name_tea),
            hydrationIndexPercentage = 80,
            icon = IconImpl(
                id = 0,
                resourceId = R.drawable.ic_apps,
            ),
        ),
    )

    fun waterTrackingChartData() = WaterTrackingChartData(
        entriesList = listOf(
            WaterTrackingChartEntry(
                from = 0,
                to = 1,
            ),
            WaterTrackingChartEntry(
                from = 2,
                to = 3,
            ),
            WaterTrackingChartEntry(
                from = 4,
                to = 5,
            ),
            WaterTrackingChartEntry(
                from = 6,
                to = 7,
            ),
        ),
    )
}