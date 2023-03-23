package hardcoder.dev.logic.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

internal const val DATA_STORE_FILE_NAME = "HEALTHER_DATA_STORE"
internal val Context.healtherDataStore: DataStore<Preferences> by preferencesDataStore(
    name = DATA_STORE_FILE_NAME
)