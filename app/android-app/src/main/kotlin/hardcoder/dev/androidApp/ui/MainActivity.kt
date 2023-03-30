package hardcoder.dev.androidApp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import hardcoder.dev.androidApp.ui.features.fasting.plans.FastingPlanResourcesProvider
import hardcoder.dev.androidApp.ui.formatters.DateTimeFormatter
import hardcoder.dev.androidApp.ui.formatters.LiquidFormatter
import hardcoder.dev.androidApp.ui.navigation.RootScreen
import hardcoder.dev.androidApp.ui.setUpFlow.gender.GenderResourcesProvider
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
                    LocalIconResolver provides IconResolver(this),
                    LocalLiquidFormatter provides LiquidFormatter(
                        context = this,
                        defaultAccuracy = LiquidFormatter.Accuracy.MILLILITERS
                    ),
                    LocalDateTimeFormatter provides DateTimeFormatter(
                        context = this,
                        defaultAccuracy = DateTimeFormatter.Accuracy.MINUTES
                    ),
                    LocalGenderResourcesProvider provides GenderResourcesProvider(),
                    LocalFastingPlanResourcesProvider provides FastingPlanResourcesProvider()
                ) {
                    RootScreen()
                }
            }
        }
    }
}