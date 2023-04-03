package hardcoder.dev.logic.features.waterTracking.drinkType

import android.content.Context
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.DrinkType
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.R
import hardcoder.dev.logic.icons.IconResourceProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DrinkTypeCreator(
    private val context: Context,
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val iconResourceProvider: IconResourceProvider
) {

    suspend fun create(
        name: String,
        iconId: Long,
        hydrationIndexPercentage: Int
    ) = withContext(dispatcher) {
        appDatabase.drinkTypeQueries.insert(
            id = idGenerator.nextId(),
            name = name,
            iconId = iconId,
            hydrationIndexPercentage = hydrationIndexPercentage
        )
    }

    suspend fun createPredefined() = withContext(dispatcher) {
        generatePredefinedDrinkTypes().forEach { drinkType ->
            appDatabase.drinkTypeQueries.insert(
                id = drinkType.id,
                name = drinkType.name,
                iconId = drinkType.iconId,
                hydrationIndexPercentage = drinkType.hydrationIndexPercentage
            )
        }
    }

    // TODO ICONS FROM DESIGNER WILL BE HERE
    private fun generatePredefinedDrinkTypes() = listOf(
        DrinkType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_drinkType_name_water),
            iconId = iconResourceProvider.getIcon(0).id,
            hydrationIndexPercentage = 100
        ),
        DrinkType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_drinkType_name_coffee),
            iconId = iconResourceProvider.getIcon(1).id,
            hydrationIndexPercentage = 50
        ),
        DrinkType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_drinkType_name_beer),
            iconId = iconResourceProvider.getIcon(2).id,
            hydrationIndexPercentage = 90
        ),
        DrinkType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_drinkType_name_milk),
            iconId = iconResourceProvider.getIcon(3).id,
            hydrationIndexPercentage = 80
        ),
        DrinkType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_drinkType_name_tea),
            iconId = iconResourceProvider.getIcon(4).id,
            hydrationIndexPercentage = 30
        ),
        DrinkType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_drinkType_name_juice),
            iconId = iconResourceProvider.getIcon(5).id,
            hydrationIndexPercentage = 80
        ),
        DrinkType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_drinkType_name_soda),
            iconId = iconResourceProvider.getIcon(6).id,
            hydrationIndexPercentage = 90
        ),
        DrinkType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_drinkType_name_soup),
            iconId = iconResourceProvider.getIcon(7).id,
            hydrationIndexPercentage = 50
        )
    )
}