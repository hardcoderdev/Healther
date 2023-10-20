package hardcoder.dev.database.dao.appPreferences

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import hardcoder.dev.database.entities.appPreferences.AppPreferences
import kotlinx.coroutines.flow.Flow

@Dao
interface AppPreferencesDao {

    @Upsert
    suspend fun upsert(appPreferences: AppPreferences)

    @Query("SELECT * FROM app_preferences")
    fun providePreferences(): Flow<AppPreferences>
}