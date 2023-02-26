package hardcoder.dev.presentation.pedometer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.extensions.createRangeForCurrentDay
import hardcoder.dev.logic.hero.HeroProvider
import hardcoder.dev.logic.pedometer.CaloriesResolver
import hardcoder.dev.logic.pedometer.KilometersResolver
import hardcoder.dev.logic.pedometer.PedometerTrackProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

class PedometerViewModel(
    heroProvider: HeroProvider,
    pedometerTrackProvider: PedometerTrackProvider,
    private val kilometersResolver: KilometersResolver,
    private val caloriesResolver: CaloriesResolver
) : ViewModel() {

    private val hero = heroProvider.requireHero()
    private val isTrackingNow = MutableStateFlow(false)
    private val totalStepsCount = pedometerTrackProvider.providePedometerTracksByRange(
        LocalDate.now().createRangeForCurrentDay(timeZone = TimeZone.currentSystemDefault())
    ).map { pedometerTracks ->
        pedometerTracks.sumOf { it.stepsCount }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0
    )
    private val dailyRateStepsCount = MutableStateFlow(DAILY_RATE_PEDOMETER)
    private val totalKilometersCount = totalStepsCount.map {
        kilometersResolver.resolve(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0.0f
    )
    private val totalCaloriesCount = hero.map {
        caloriesResolver.resolve(it.weight)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0.0f
    )

    val state = combine(
        isTrackingNow,
        totalStepsCount,
        totalKilometersCount,
        totalCaloriesCount,
        dailyRateStepsCount
    ) { isTrackingNow, totalStepsCount, totalKilometersCount, totalCaloriesCount, dailyRateStepsCount ->
        State(
            isTrackingNow,
            totalStepsCount,
            totalKilometersCount,
            totalCaloriesCount,
            dailyRateStepsCount
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            isTrackingNow.value,
            totalStepsCount.value,
            totalKilometersCount.value,
            totalCaloriesCount.value,
            dailyRateStepsCount.value
        )
    )

    fun updateTrackingStatus(isTracking: Boolean) {
        isTrackingNow.value = isTracking
    }

    sealed class FetchingState {
        object NotFetched : FetchingState()
        data class Fetched(val state: State)
    }

    data class State(
        val isTrackingNow: Boolean,
        val totalStepsCount: Int,
        val totalKilometersCount: Float,
        val totalCaloriesBurned: Float,
        val dailyRateStepsCount: Int
    )

    internal companion object {
        private const val DAILY_RATE_PEDOMETER = 6000
    }
}