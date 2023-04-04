package hardcoder.dev.presentation.features.waterTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.extensions.createRangeForCurrentDay
import hardcoder.dev.extensions.mapItems
import hardcoder.dev.logic.features.waterTracking.WaterPercentageResolver
import hardcoder.dev.logic.features.waterTracking.WaterTrackProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class WaterTrackingHistoryViewModel(
    private val waterTrackProvider: WaterTrackProvider,
    private val waterPercentageResolver: WaterPercentageResolver
) : ViewModel() {

    private val selectedRangeStateFlow =
        MutableStateFlow(LocalDate.now().createRangeForCurrentDay())
    val state = selectedRangeStateFlow.flatMapLatest { range ->
        waterTrackProvider.provideWaterTracksByDayRange(range)
    }.mapItems {
        it.toItem(
            resolvedMillilitersCount = waterPercentageResolver.resolve(
                drinkType = it.drinkType,
                millilitersDrunk = it.millilitersCount
            )
        )
    }.map {
        State(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            waterTrackItems = emptyList()
        )
    )

    fun selectRange(range: ClosedRange<Instant>) {
        selectedRangeStateFlow.value = range
    }

    data class State(val waterTrackItems: List<WaterTrackItem>)
}