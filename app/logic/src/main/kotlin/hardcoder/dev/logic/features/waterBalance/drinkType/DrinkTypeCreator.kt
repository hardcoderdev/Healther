package hardcoder.dev.logic.features.waterBalance.drinkType

import android.content.Context
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.DrinkType
import hardcoder.dev.database.IdGenerator
import hardcoder.dev.logic.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DrinkTypeCreator(
    private val context: Context,
    private val idGenerator: IdGenerator,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun create(
        name: String,
        iconResourceName: String,
        hydrationIndexPercentage: Int
    ) = withContext(dispatcher) {
        appDatabase.drinkTypeQueries.insert(
            id = idGenerator.nextId(),
            name = name,
            iconResourceName = iconResourceName,
            hydrationIndexPercentage = hydrationIndexPercentage
        )
    }

    suspend fun createPredefined() = withContext(dispatcher) {
        generatePredefinedDrinkTypes().forEach { drinkType ->
            appDatabase.drinkTypeQueries.insert(
                id = drinkType.id,
                name = drinkType.name,
                iconResourceName = drinkType.iconResourceName,
                hydrationIndexPercentage = drinkType.hydrationIndexPercentage
            )
        }
    }

    // TODO ICONS FROM DESIGNER WILL BE HERE
    private fun generatePredefinedDrinkTypes() = listOf(
        DrinkType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_drinkType_name_water),
            iconResourceName = "ic_mma",
            hydrationIndexPercentage = 100
        ),
        DrinkType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_drinkType_name_coffee),
            iconResourceName = "ic_mma",
            hydrationIndexPercentage = 50
        ),
        DrinkType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_drinkType_name_beer),
            iconResourceName = "ic_mma",
            hydrationIndexPercentage = 90
        ),
        DrinkType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_drinkType_name_milk),
            iconResourceName = "ic_mma",
            hydrationIndexPercentage = 80
        ),
        DrinkType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_drinkType_name_tea),
            iconResourceName = "ic_mma",
            hydrationIndexPercentage = 30
        ),
        DrinkType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_drinkType_name_juice),
            iconResourceName = "ic_mma",
            hydrationIndexPercentage = 80
        ),
        DrinkType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_drinkType_name_soda),
            iconResourceName = "ic_mma",
            hydrationIndexPercentage = 90
        ),
        DrinkType(
            id = idGenerator.nextId(),
            name = context.getString(R.string.predefined_drinkType_name_soup),
            iconResourceName = "ic_mma",
            hydrationIndexPercentage = 50
        )
    )
}