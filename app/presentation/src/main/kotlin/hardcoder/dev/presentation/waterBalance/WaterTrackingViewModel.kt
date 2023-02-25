package hardcoder.dev.presentation.waterBalance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.entities.waterTracking.DrinkType
import hardcoder.dev.entities.hero.Hero
import hardcoder.dev.extensions.getEndOfDay
import hardcoder.dev.extensions.getStartOfDay
import hardcoder.dev.extensions.mapItems
import hardcoder.dev.logic.hero.HeroProvider
import hardcoder.dev.logic.waterBalance.WaterTrackDeleter
import hardcoder.dev.logic.waterBalance.WaterTrackProvider
import hardcoder.dev.logic.waterBalance.resolvers.WaterIntakeResolver
import hardcoder.dev.logic.waterBalance.resolvers.WaterPercentageResolver
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class WaterTrackingViewModel(
    heroProvider: HeroProvider,
    private val waterTrackProvider: WaterTrackProvider,
    private val waterTrackDeleter: WaterTrackDeleter,
    private val waterIntakeResolver: WaterIntakeResolver,
    private val waterPercentageResolver: WaterPercentageResolver
) : ViewModel() {

    private val millilitersDrunk = MutableStateFlow(0)
    private val dailyWaterIntake = MutableStateFlow(0)
    private val waterTracksList = MutableStateFlow<List<WaterTrackItem>>(emptyList())
    private val hero = heroProvider.requireHero()

    val state = combine(
        millilitersDrunk,
        waterTracksList,
        dailyWaterIntake,
        hero
    ) { millilitersDrunk, waterTracks, dailyWaterIntake, hero ->
        LoadingState.Loaded(
            State(
                millisCount = millilitersDrunk,
                waterTracks = waterTracks,
                dailyWaterIntake = dailyWaterIntake,
                hero = hero
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

    fun delete(waterTrackId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            waterTrackDeleter.deleteById(waterTrackId)
        }
    }

    data class State(
        val hero: Hero,
        val millisCount: Int,
        val dailyWaterIntake: Int,
        val waterTracks: List<WaterTrackItem>
    )

    sealed class LoadingState {
        object Loading : LoadingState()
        data class Loaded(val state: State) : LoadingState()
    }
}