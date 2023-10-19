package hardcoder.dev.database.dao.features.foodTracking

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hardcoder.dev.database.entities.features.foodTracking.FoodType
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foodType: FoodType)

    @Update
    suspend fun update(foodType: FoodType)

    @Query("DELETE FROM food_types WHERE id = :foodTypeId")
    suspend fun deleteById(foodTypeId: Int)

    @Query("SELECT * FROM food_types")
    fun provideAllFoodTypes(): Flow<List<FoodType>>

    @Query("SELECT * FROM food_types WHERE id = :foodTypeId")
    fun provideFoodTypeById(foodTypeId: Int): Flow<FoodType>
}