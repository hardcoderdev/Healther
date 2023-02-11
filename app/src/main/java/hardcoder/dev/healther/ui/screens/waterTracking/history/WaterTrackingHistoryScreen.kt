@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package hardcoder.dev.healther.ui.screens.waterTracking.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.healther.R
import hardcoder.dev.healther.data.local.room.entities.WaterTrack
import hardcoder.dev.healther.logic.DrinkTypeImageResolver
import hardcoder.dev.healther.ui.base.LocalPresentationModule
import hardcoder.dev.healther.ui.base.composables.ScaffoldWrapper
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import kotlinx.datetime.toKotlinLocalDate
import java.time.LocalDate

@Composable
fun WaterTrackingHistoryScreen(
    onGoBack: () -> Unit,
    onTrackUpdate: (WaterTrack) -> Unit
) {
    ScaffoldWrapper(
        titleResId = R.string.detail_history,
        content = { WaterTrackingContent(onTrackUpdate = onTrackUpdate) },
        onGoBack = onGoBack
    )
}

@Composable
fun WaterTrackingContent(onTrackUpdate: (WaterTrack) -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.createWaterTrackingHistoryViewModel()
    }

    val state = viewModel.state.collectAsState()
    val calendarState = rememberSelectableCalendarState(initialSelectionMode = SelectionMode.Single)

    if (calendarState.selectionState.selection.isNotEmpty()) {
        viewModel.fetchWaterTracks(
            calendarState.selectionState.selection.first().toKotlinLocalDate()
        )
    } else {
        viewModel.fetchWaterTracks(LocalDate.now().toKotlinLocalDate())
    }

    Column(Modifier.padding(16.dp)) {
        SelectableCalendar(calendarState = calendarState)
        Spacer(modifier = Modifier.height(16.dp))
        WaterTracksHistory(
            waterTracks = state.value,
            onTrackUpdate = onTrackUpdate,
            onTrackDelete = { waterTrack ->
                viewModel.deleteTrack(waterTrack)
            }
        )
    }
}

@Composable
fun WaterTracksHistory(
    waterTracks: List<WaterTrack>,
    onTrackDelete: (WaterTrack) -> Unit,
    onTrackUpdate: (WaterTrack) -> Unit
) {
    if (waterTracks.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(waterTracks) { track ->
                WaterTrackItem(
                    waterTrack = track,
                    onDelete = onTrackDelete,
                    onUpdate = onTrackUpdate
                )
            }
        }
    } else {
        Text(
            text = stringResource(id = R.string.empty_history_day_label),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun WaterTrackItem(
    waterTrack: WaterTrack,
    onDelete: (WaterTrack) -> Unit,
    onUpdate: (WaterTrack) -> Unit
) {
    val drinkTypeImageResolver = DrinkTypeImageResolver()

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp, pressedElevation = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth(),
        onClick = { onUpdate(waterTrack) }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = drinkTypeImageResolver.resolve(waterTrack.drinkType)),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            ) {
                Text(
                    text = stringResource(id = waterTrack.drinkType.transcriptionResId),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(
                        id = R.string.milliliters_format,
                        waterTrack.millilitersCount
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            IconButton(
                modifier = Modifier.weight(0.5f),
                onClick = { onDelete(waterTrack) }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.delete_track_cd)
                )
            }
        }
    }
}

@Preview
@Composable
fun WaterTrackingContentPreview() {
    WaterTrackingHistoryScreen(onGoBack = {}, onTrackUpdate = {})
}

