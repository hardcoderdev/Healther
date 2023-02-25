package hardcoder.dev.android_ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import hardcoder.dev.android_ui.theme.HealtherTheme
import hardcoder.dev.android_ui.features.waterBalance.DrinkTypeResourcesProvider

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HealtherTheme {
                CompositionLocalProvider(
                    LocalPresentationModule provides App.instance.presentationModule,
                    LocalDrinkTypeResourcesProvider provides DrinkTypeResourcesProvider()
                ) {
                    RootScreen()
                }
            }
        }
    }
}