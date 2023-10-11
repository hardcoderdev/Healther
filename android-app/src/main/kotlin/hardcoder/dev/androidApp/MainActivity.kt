package hardcoder.dev.androidApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import hardcoder.dev.androidApp.ui.navigation.RootScreen
import hardcoder.dev.pedometer_manager.BatteryRequirementsController
import hardcoder.dev.permissions.PermissionsController
import hardcoder.dev.uikit.values.HealtherTheme
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindControllers()

        setContent {
            HealtherTheme {
                RootScreen()
            }
        }
    }
}

private fun ComponentActivity.bindControllers() {
    get<PermissionsController>().bind(this)
    get<BatteryRequirementsController>().bind(this)
}