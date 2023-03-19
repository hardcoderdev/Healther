package hardcoder.dev.androidApp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import hardcoder.dev.androidApp.ui.features.starvation.plans.StarvationPlanResourcesProvider
import hardcoder.dev.androidApp.ui.features.waterBalance.DrinkTypeResourcesProvider
import hardcoder.dev.androidApp.ui.navigation.RootScreen
import hardcoder.dev.androidApp.ui.theme.HealtherTheme
import hardcoder.dev.datetime.TimeUnitMapper

class MainActivity : ComponentActivity() {

    private val permissionsController by lazy {
        App.instance.presentationModule.logicModule.permissionsController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionsController.bind(this)

        setContent {
            HealtherTheme {
                CompositionLocalProvider(
                    LocalPresentationModule provides App.instance.presentationModule,
                    LocalDrinkTypeResourcesProvider provides DrinkTypeResourcesProvider(),
                    LocalFloatFormatter provides FloatFormatter(),
                    LocalDateTimeFormatter provides DateTimeFormatter(
                        context = this,
                        defaultAccuracy = DateTimeFormatter.Accuracy.MINUTES
                    ),
                    LocalStarvationPlanResourcesProvider provides StarvationPlanResourcesProvider(),
                    LocalTimeUnitMapper provides TimeUnitMapper()
                ) {
                    RootScreen()
                }
            }
        }
    }
}