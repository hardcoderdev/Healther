@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

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
import hardcoder.dev.healther.ui.base.composables.TopBarConfig
import hardcoder.dev.healther.ui.base.composables.TopBarType
import hardcoder.dev.healther.ui.base.extensions.getEndOfDay
import hardcoder.dev.healther.ui.base.extensions.getStartOfDay
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate

@Composable
fun WaterTrackingHistoryScreen(
    onGoBack: () -> Unit,
    onTrackUpdate: (WaterTrack) -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.createWaterTrackingHistoryViewModel()
    }

    val state = viewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            WaterTrackingContent(
                state = state.value,
                onTrackUpdate = onTrackUpdate,
                onTrackDelete = viewModel::deleteTrack,
                onFetchWaterTracks = {
                    viewModel.selectRange(it.getStartOfDay()..it.getEndOfDay())
                }
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.waterHistory_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
fun WaterTrackingContent(
    state: WaterTrackingHistoryViewModel.State,
    onFetchWaterTracks: (LocalDate) -> Unit,
    onTrackUpdate: (WaterTrack) -> Unit,
    onTrackDelete: (WaterTrack) -> Unit
) {
    val calendarState = rememberSelectableCalendarState(initialSelectionMode = SelectionMode.Single)

    if (calendarState.selectionState.selection.isNotEmpty()) {
        onFetchWaterTracks(calendarState.selectionState.selection.first().toKotlinLocalDate())
    } else {
        onFetchWaterTracks(LocalDate.now())
    }

    Column(Modifier.padding(16.dp)) {
        SelectableCalendar(calendarState = calendarState)
        Spacer(modifier = Modifier.height(16.dp))
        WaterTracksHistory(
            waterTracks = state.drinks,
            onTrackUpdate = onTrackUpdate,
            onTrackDelete = onTrackDelete
        )
    }
}

@Composable
private fun WaterTracksHistory(
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
            text = stringResource(id = R.string.waterHistory_emptyDayHistory_text),
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
                        id = R.string.waterTrackItem_formatMilliliters_text,
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
                    contentDescription = stringResource(id = R.string.waterTrackItem_deleteTrack_iconContentDescription)
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

