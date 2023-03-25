package hardcoder.dev.androidApp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import hardcoder.dev.androidApp.ui.features.starvation.plans.StarvationPlanResourcesProvider
import hardcoder.dev.androidApp.ui.navigation.RootScreen
import hardcoder.dev.androidApp.ui.setUpFlow.gender.GenderResourcesProvider
import hardcoder.dev.datetime.TimeUnitMapper
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
                    LocalIconProvider provides IconProvider(this),
                    LocalIconResolver provides IconResolver(this),
                    LocalFloatFormatter provides FloatFormatter(),
                    LocalDateTimeFormatter provides DateTimeFormatter(
                        context = this,
                        defaultAccuracy = DateTimeFormatter.Accuracy.MINUTES
                    ),
                    LocalGenderResourcesProvider provides GenderResourcesProvider(),
                    LocalStarvationPlanResourcesProvider provides StarvationPlanResourcesProvider(),
                    LocalTimeUnitMapper provides TimeUnitMapper()
                ) {
                    RootScreen()
                }
            }
        }
    }
}