import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private const val DATA_STORE_FILE_NAME = "HEALTHER_DATA_STORE"
val Context.healtherDataStore: DataStore<Preferences> by preferencesDataStore(
    name = DATA_STORE_FILE_NAME,
)