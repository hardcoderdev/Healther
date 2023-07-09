package hardcoder.dev.permissions

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class EmptyPermissionsException : IllegalArgumentException("Permissions for the request were not passed")

class PermissionsController {

    private val mutex = Mutex()
    private val lastResult = MutableStateFlow<Map<String, Boolean>?>(null)
    private lateinit var launcher: ActivityResultLauncher<Array<String>>

    fun bind(activity: ComponentActivity) {
        launcher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) {
            lastResult.value = it
        }
    }

    suspend fun requestPermissions(
        permissions: Array<String>,
    ): Map<String, Boolean> {
        mutex.withLock {
            if (permissions.isEmpty()) throw EmptyPermissionsException()
            launcher.launch(permissions)
            return lastResult.filterNotNull().first().also {
                lastResult.value = null
            }
        }
    }
}