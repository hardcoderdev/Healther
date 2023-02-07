package hardcoder.dev.healther.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hardcoder.dev.healther.ui.base.LocalPresentationModule
import hardcoder.dev.healther.ui.screens.RootScreen
import hardcoder.dev.healther.ui.screens.waterTracking.WaterTrackingScreen
import hardcoder.dev.healther.ui.theme.HealtherTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealtherTheme {
                CompositionLocalProvider(LocalPresentationModule provides App.instance.presentationModule) {
                    RootScreen()
                }
            }
        }
    }
}