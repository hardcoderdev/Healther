package hardcoder.dev.healther.ui.screens.waterTracking

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.healther.R
import hardcoder.dev.healther.ui.base.LocalPresentationModule
import hardcoder.dev.healther.ui.base.composables.CircularProgressBar
import hardcoder.dev.healther.ui.base.composables.ScaffoldWrapper

@Composable
fun WaterTrackingScreen(onGoBack: () -> Unit, onSaveWaterTrack: () -> Unit) {
    ScaffoldWrapper(
        titleResId = R.string.water_tracking_title,
        content = { WaterTrackingContent(onSaveWaterTrack = onSaveWaterTrack) },
        onGoBack = onGoBack
    )
}

@Composable
fun WaterTrackingContent(onSaveWaterTrack: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val waterTrackingViewModel = viewModel {
        presentationModule.createWaterTrackingViewModel()
    }

    val userViewModel = viewModel {
        presentationModule.createUserViewModel()
    }

    val userDataState = userViewModel.userData.collectAsState()
    waterTrackingViewModel.resolveDailyWaterIntake(
        userDataState.value.weight,
        userDataState.value.stressTime,
        userDataState.value.gender
    )

    val waterTrackingState = waterTrackingViewModel.state.collectAsState()
    val dailyWaterIntake = waterTrackingState.value.dailyWaterIntake
    val millisCount = waterTrackingState.value.millisCount

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.tap_to_add_record),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        CircularProgressBar(
            percentage = if (millisCount != 0) (dailyWaterIntake / millisCount * 100).toFloat() else 0.0f,
            number = waterTrackingState.value.dailyWaterIntake,
            modifier = Modifier.clickable {
                onSaveWaterTrack()
            }
        )
    }
}

@Preview
@Composable
fun WaterTrackingScreenPreview() {
    WaterTrackingScreen(onGoBack = { }, onSaveWaterTrack = {})
}