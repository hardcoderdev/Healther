package hardcoder.dev.androidApp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import hardcoder.dev.androidApp.di.LocalLogicModule
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.androidApp.di.LocalUIModule
import hardcoder.dev.androidApp.ui.navigation.RootScreen
import hardcoder.dev.uikit.HealtherTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val logicModule = App.instance.presentationModule.logicModule
        logicModule.permissionsController.bind(this)
        logicModule.batteryRequirementsController.bind(this)

        setContent {
            HealtherTheme {
                CompositionLocalProvider(
                    LocalPresentationModule provides App.instance.presentationModule,
                    LocalLogicModule provides App.instance.logicModule,
                    LocalUIModule provides App.instance.uiModule
                ) {
                    RootScreen()
                }
            }
        }
    }
}