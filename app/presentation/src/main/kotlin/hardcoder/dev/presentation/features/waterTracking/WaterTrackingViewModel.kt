package hardcoder.dev.presentation.features.waterTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.mapItems
import hardcoder.dev.datetime.createRangeForCurrentDay
import hardcoder.dev.datetime.millisToLocalDateTime
import hardcoder.dev.logic.features.waterTracking.WaterPercentageResolver
import hardcoder.dev.logic.features.waterTracking.WaterTrackProvider
import hardcoder.dev.logic.features.waterTracking.WaterTrackingDailyRateProvider
import hardcoder.dev.logic.features.waterTracking.WaterTrackingMillilitersDrunkProvider
import hardcoder.dev.logic.features.waterTracking.statistic.WaterTrackingStatistic
import hardcoder.dev.logic.features.waterTracking.statistic.WaterTrackingStatisticProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate

class WaterTrackingViewModel(
    waterTrackProvider: WaterTrackProvider,
    waterPercentageResolver: WaterPercentageResolver,
    waterTrackingStatisticProvider: WaterTrackingStatisticProvider,
    dailyRateProvider: WaterTrackingDailyRateProvider,
    millilitersDrunkProvider: WaterTrackingMillilitersDrunkProvider
) : ViewModel() {

    private val waterTracksList = waterTrackProvider.provideWaterTracksByDayRange(
        LocalDate.now().createRangeForCurrentDay()
    ).mapItems { waterTrack ->
        waterTrack.toItem(
            resolvedMillilitersCount = waterPercentageResolver.resolve(
                drinkType = waterTrack.drinkType,
                millilitersDrunk = waterTrack.millilitersCount
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    private val chartEntries = waterTracksList.map { waterTrackList ->
        waterTrackList.groupBy {
            it.timeInMillis.millisToLocalDateTime().hour
        }.map { entry ->
            entry.key to entry.value.sumOf { it.millilitersCount }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = listOf(0 to 0)
    )

    val state = combine(
        millilitersDrunkProvider.provideMillilitersDrunkToday(),
        waterTracksList,
        dailyRateProvider.provideDailyRateInMilliliters(),
        chartEntries,
        waterTrackingStatisticProvider.provideWaterTrackingStatistic()
    ) { millilitersDrunk, waterTracks, dailyWaterIntake, chartEntries, statistic ->
        LoadingState.Loaded(
            State(
                millisCount = millilitersDrunk,
                waterTracks = waterTracks,
                dailyWaterIntake = dailyWaterIntake,
                chartEntries = chartEntries,
                statistic = statistic
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = LoadingState.Loading
    )

    data class State(
        val millisCount: Int,
        val dailyWaterIntake: Int,
        val waterTracks: List<WaterTrackingItem>,
        val chartEntries: List<Pair<Int, Int>>,
        val statistic: WaterTrackingStatistic?
    )

    sealed class LoadingState {
        object Loading : LoadingState()
        data class Loaded(val state: State) : LoadingState()
    }
}