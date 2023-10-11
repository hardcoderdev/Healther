package hardcoder.dev.identification

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.int

class IdGenerator(
    context: Context,
    settings: Settings = SharedPreferencesSettings(
        // TODO KMM
        delegate = context.getSharedPreferences("MAIN_PREFS", 0),
    ),
) {

    private var lastId by settings.int(LAST_ID_KEY, 0)

    fun nextId(): Int {
        Settings
        val newId = lastId + 1
        lastId = newId
        return newId
    }

    fun forceLastId(lastId: Int) {
        this.lastId = lastId
    }

    companion object {
        private const val LAST_ID_KEY = "lastGeneratedId"
    }
}