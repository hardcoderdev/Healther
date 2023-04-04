package hardcoder.dev.presentation.features.waterTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.extensions.getEndOfDay
import hardcoder.dev.extensions.getStartOfDay
import hardcoder.dev.extensions.mapItems
import hardcoder.dev.extensions.millisToLocalDateTime
import hardcoder.dev.logic.features.waterTracking.WaterIntakeResolver
import hardcoder.dev.logic.features.waterTracking.WaterPercentageResolver
import hardcoder.dev.logic.features.waterTracking.WaterTrackProvider
import hardcoder.dev.logic.features.waterTracking.statistic.WaterTrackingStatistic
import hardcoder.dev.logic.features.waterTracking.statistic.WaterTrackingStatisticProvider
import hardcoder.dev.logic.hero.Hero
import hardcoder.dev.logic.hero.HeroProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class WaterTrackingViewModel(
    private val waterTrackProvider: WaterTrackProvider,
    private val waterIntakeResolver: WaterIntakeResolver,
    private val waterPercentageResolver: WaterPercentageResolver,
    heroProvider: HeroProvider,
    waterTrackingStatisticProvider: WaterTrackingStatisticProvider
) : ViewModel() {

    private val millilitersDrunk = MutableStateFlow(0)
    private val dailyWaterIntake = MutableStateFlow(0)
    private val waterTracksList = MutableStateFlow<List<WaterTrackItem>>(emptyList())
    private val hero = heroProvider.requireHero()

    private val waterTrackingStatistic =
        waterTrackingStatisticProvider.provideWaterTrackingStatistic().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
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
        millilitersDrunk,
        waterTracksList,
        dailyWaterIntake,
        hero,
        chartEntries,
        waterTrackingStatistic
    ) { millilitersDrunk, waterTracks, dailyWaterIntake, hero, chartEntries, statistic ->
        LoadingState.Loaded(
            State(
                millisCount = millilitersDrunk,
                waterTracks = waterTracks,
                dailyWaterIntake = dailyWaterIntake,
                hero = hero,
                chartEntries = chartEntries,
                statistic = statistic
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = LoadingState.Loading
    )

    init {
        resolveDailyWaterIntake()
        fetchWaterTracks()
    }

    private fun fetchWaterTracks() {
        viewModelScope.launch {
            val currentDay = LocalDate.now()
            val startOfCurrentDay = currentDay.getStartOfDay()
            val endOfCurrentDay = currentDay.getEndOfDay()
            waterTrackProvider.provideWaterTracksByDayRange(startOfCurrentDay..endOfCurrentDay)
                .mapItems { waterTrack ->
                    waterTrack.toItem(
                        resolvedMillilitersCount = waterPercentageResolver.resolve(
                            drinkType = waterTrack.drinkType,
                            millilitersDrunk = waterTrack.millilitersCount
                        )
                    )
                }
                .collectLatest { waterTracks ->
                    val millilitersCount = waterTracks.sumOf { it.resolvedMillilitersCount }
                    millilitersDrunk.value = millilitersCount
                    waterTracksList.value = waterTracks
                }
        }
    }

    private fun resolveDailyWaterIntake() {
        viewModelScope.launch {
            val currentHero = hero.first()

            dailyWaterIntake.value = waterIntakeResolver.resolve(
                currentHero.weight,
                currentHero.exerciseStressTime,
                currentHero.gender
            )
        }
    }

    data class State(
        val hero: Hero,
        val millisCount: Int,
        val dailyWaterIntake: Int,
        val waterTracks: List<WaterTrackItem>,
        val chartEntries: List<Pair<Int, Int>>,
        val statistic: WaterTrackingStatistic?
    )

    sealed class LoadingState {
        object Loading : LoadingState()
        data class Loaded(val state: State) : LoadingState()
    }
}