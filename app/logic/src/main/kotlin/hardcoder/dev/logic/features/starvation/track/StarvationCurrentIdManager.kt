package hardcoder.dev.logic.features.starvation.track

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class StarvationCurrentIdManager(private val context: Context) {

    private val Context.starvationDataStore: DataStore<Preferences> by preferencesDataStore(
        name = STARVATION_DATA
    )
    private val STARVATION_CURRENT_TRACK_KEY = intPreferencesKey(STARVATION_CURRENT_TRACK_ID)

    val starvationCurrentTrackId = context.starvationDataStore.data.map {
        it[STARVATION_CURRENT_TRACK_KEY] ?: 0
    }

    suspend fun setCurrentId(id: Int?) {
        context.starvationDataStore.edit { starvationData ->
            id?.let {
                starvationData[STARVATION_CURRENT_TRACK_KEY] = it
            } ?: run {
                starvationData.clear()
            }
        }
    }

    private companion object {
        private const val STARVATION_DATA = "HEALTHER_STARVATION_DATA"
        private const val STARVATION_CURRENT_TRACK_ID = "starvation_currentTrack_id"
    }
}