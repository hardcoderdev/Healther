package hardcoder.dev.androidApp.ui.features.waterTracking.drinkType.providers

import android.content.Context
import androidx.annotation.StringRes
import hardcoder.dev.logic.R
import hardcoder.dev.logic.features.waterTracking.drinkType.DrinkTypePredefined
import hardcoder.dev.logic.features.waterTracking.drinkType.PredefinedDrinkTypeProvider

class PredefinedDrinkTypeProviderImpl(
    private val context: Context,
    private val drinkTypeIconProvider: DrinkTypeIconProvider
) : PredefinedDrinkTypeProvider {

    override fun providePredefined(): List<DrinkTypePredefined> {
        return listOf(
            create(
                nameResId = R.string.predefined_drinkType_name_water,
                iconId = 0,
                hydrationIndexPercentage = 100
            ),
            create(
                nameResId = R.string.predefined_drinkType_name_coffee,
                iconId = 1,
                hydrationIndexPercentage = 50
            ),
            create(
                nameResId = R.string.predefined_drinkType_name_beer,
                iconId = 2,
                hydrationIndexPercentage = 90
            ),
            create(
                nameResId = R.string.predefined_drinkType_name_milk,
                iconId = 3,
                hydrationIndexPercentage = 80
            ),
            create(
                nameResId = R.string.predefined_drinkType_name_tea,
                iconId = 4,
                hydrationIndexPercentage = 30
            ),
            create(
                nameResId = R.string.predefined_drinkType_name_juice,
                iconId = 5,
                hydrationIndexPercentage = 80
            ),
            create(
                nameResId = R.string.predefined_drinkType_name_soda,
                iconId = 6,
                hydrationIndexPercentage = 90
            ),
            create(
                nameResId = R.string.predefined_drinkType_name_soup,
                iconId = 7,
                hydrationIndexPercentage = 50
            )
        )
    }

    private fun create(
        @StringRes nameResId: Int,
        iconId: Long,
        hydrationIndexPercentage: Int
    ) = DrinkTypePredefined(
        name = context.getString(nameResId),
        icon = drinkTypeIconProvider.getIcon(iconId),
        hydrationIndexPercentage = hydrationIndexPercentage
    )
}