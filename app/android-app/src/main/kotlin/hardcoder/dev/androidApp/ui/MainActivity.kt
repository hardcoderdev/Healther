package hardcoder.dev.androidApp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import hardcoder.dev.androidApp.ui.features.fasting.plans.FastingPlanResourcesProvider
import hardcoder.dev.androidApp.ui.navigation.RootScreen
import hardcoder.dev.androidApp.ui.setUpFlow.gender.GenderResourcesProvider
import hardcoder.dev.datetime.TimeUnitMapper
import hardcoder.dev.uikit.HealtherTheme

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
                    LocalIconResolver provides IconResolver(this),
                    LocalFloatFormatter provides FloatFormatter(),
                    LocalDateTimeFormatter provides DateTimeFormatter(
                        context = this,
                        defaultAccuracy = DateTimeFormatter.Accuracy.MINUTES
                    ),
                    LocalGenderResourcesProvider provides GenderResourcesProvider(),
                    LocalFastingPlanResourcesProvider provides FastingPlanResourcesProvider(),
                    LocalTimeUnitMapper provides TimeUnitMapper()
                ) {
                    RootScreen()
                }
            }
        }
    }
}