package hardcoder.dev.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.entities.DrinkType
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

data class WaterTrackItem(
    val id: Int,
    val drinkType: DrinkType,
    val millilitersCount: Int,
    val resolvedMillilitersCount: Int,
    val timeInMillis: Long
)

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
    private val weight = hero.map {
        it.weight
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0
    )

    private val exerciseStressTime = hero.map {
        it.exerciseStressTime
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0
    )

    private val gender = hero.map {
        it.gender
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = hardcoder.dev.entities.Gender.MALE
    )

    val state = combine(
        millilitersDrunk,
        waterTracksList,
        dailyWaterIntake,
        weight,
        exerciseStressTime,
        gender
    ) { millilitersDrunk, waterTracks, dailyWaterIntake, weight, exerciseStressTime, gender ->
        LoadingState.Loaded(
            State(
                millisCount = millilitersDrunk,
                waterTracks = waterTracks,
                dailyWaterIntake = dailyWaterIntake,
                weight = weight,
                exerciseStressTime = exerciseStressTime,
                gender = gender
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
        dailyWaterIntake.value = waterIntakeResolver.resolve(
            weight.value,
            exerciseStressTime.value,
            gender.value
        )
    }

    fun delete(waterTrackId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            waterTrackDeleter.deleteById(waterTrackId)
        }
    }

    data class State(
        val weight: Int,
        val exerciseStressTime: Int,
        val gender: hardcoder.dev.entities.Gender,
        val millisCount: Int,
        val dailyWaterIntake: Int,
        val waterTracks: List<WaterTrackItem>
    )

    sealed class LoadingState {
        object Loading : LoadingState()
        data class Loaded(val state: State) : LoadingState()
    }
}