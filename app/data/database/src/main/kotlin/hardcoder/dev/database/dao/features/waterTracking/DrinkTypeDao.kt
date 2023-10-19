package hardcoder.dev.database.dao.features.waterTracking

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hardcoder.dev.database.entities.features.waterTracking.DrinkType
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinkTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(drinkType: DrinkType)

    @Update
    suspend fun update(drinkType: DrinkType)

    @Query("DELETE FROM drink_types WHERE id = :drinkTypeId")
    suspend fun deleteById(drinkTypeId: Int)

    @Query("SELECT * FROM drink_types")
    fun provideAllDrinkTypes(): Flow<List<DrinkType>>

    @Query("SELECT * FROM drink_types WHERE id = :drinkTypeId")
    fun provideDrinkTypeById(drinkTypeId: Int): Flow<DrinkType>
}