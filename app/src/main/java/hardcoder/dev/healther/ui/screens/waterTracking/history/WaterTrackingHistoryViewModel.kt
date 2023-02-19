package hardcoder.dev.healther.ui.screens.waterTracking.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.healther.logic.resolvers.DrinkTypeImageResolver
import hardcoder.dev.healther.logic.resolvers.WaterPercentageResolver
import hardcoder.dev.healther.repository.WaterTrackRepository
import hardcoder.dev.healther.ui.base.extensions.createRangeForCurrentDay
import hardcoder.dev.healther.ui.base.extensions.mapItems
import hardcoder.dev.healther.ui.base.extensions.toItem
import hardcoder.dev.healther.ui.screens.waterTracking.WaterTrackItem
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class WaterTrackingHistoryViewModel(
    private val waterTrackRepository: WaterTrackRepository,
    private val drinkTypeImageResolver: DrinkTypeImageResolver,
    private val waterPercentageResolver: WaterPercentageResolver
) : ViewModel() {

    private val selectedRangeStateFlow =
        MutableStateFlow(LocalDate.now().createRangeForCurrentDay())
    val state = selectedRangeStateFlow.flatMapLatest { range ->
        waterTrackRepository.getWaterTracksByDayRange(range)
    }.mapItems {
        it.toItem(
            drinkTypeImageResolver.resolve(it.drinkType),
            waterPercentageResolver.resolve(
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

    fun selectRange(range: LongRange) {
        selectedRangeStateFlow.value = range
    }

    fun deleteTrack(waterTrackId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            waterTrackRepository.delete(waterTrackId)
        }
    }

    data class State(val waterTrackItems: List<WaterTrackItem>)
}