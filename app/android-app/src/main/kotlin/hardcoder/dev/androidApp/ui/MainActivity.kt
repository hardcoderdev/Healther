package hardcoder.dev.androidApp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import hardcoder.dev.androidApp.ui.features.pedometer.logic.BatteryRequirementsController
import hardcoder.dev.androidApp.ui.navigation.RootScreen
import hardcoder.dev.in_app_review.AndroidReviewManager
import hardcoder.dev.permissions.PermissionsController
import hardcoder.dev.uikit.HealtherTheme
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
    get<AndroidReviewManager>().bind(this)
}