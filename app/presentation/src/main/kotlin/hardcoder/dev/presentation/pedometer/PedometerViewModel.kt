package hardcoder.dev.presentation.pedometer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.extensions.createRangeForCurrentDay
import hardcoder.dev.logic.pedometer.CaloriesResolver
import hardcoder.dev.logic.pedometer.KilometersResolver
import hardcoder.dev.logic.pedometer.MinutesResolver
import hardcoder.dev.logic.pedometer.PedometerTrackProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class PedometerViewModel(
    pedometerTrackProvider: PedometerTrackProvider,
    private val kilometersResolver: KilometersResolver,
    private val caloriesResolver: CaloriesResolver,
    private val minutesResolver: MinutesResolver
) : ViewModel() {

    private val isTrackingNow = MutableStateFlow(false)
    private val totalWastedTime = MutableStateFlow(0L)
    private val totalKilometersCount = MutableStateFlow(0.0f)
    private val totalCaloriesCount = MutableStateFlow(0.0f)

    private val totalStepsCount = pedometerTrackProvider.providePedometerTracksByRange(
        LocalDate.now().createRangeForCurrentDay(timeZone = TimeZone.currentSystemDefault())
    ).map { pedometerTracks ->
        val totalStepsCount = pedometerTracks.sumOf { it.stepsCount }

        totalCaloriesCount.value = caloriesResolver.resolve(totalStepsCount)
        totalKilometersCount.value = kilometersResolver.resolve(totalStepsCount)
        totalWastedTime.value = pedometerTracks.sumOf {
            val startTimeInLocalDate = it.range.first
            val endTimeInLocalDate = it.range.last
            if (endTimeInLocalDate.hours > startTimeInLocalDate.hours) {
                val differenceInMillis = endTimeInLocalDate - startTimeInLocalDate
                differenceInMillis / 1000 / 60
            } else {
                0
            }
        }

        totalStepsCount
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0
    )

    private val dailyRateStepsCount = MutableStateFlow(DAILY_RATE_PEDOMETER)

    val state = combine(
        isTrackingNow,
        totalStepsCount,
        totalKilometersCount,
        totalCaloriesCount,
        totalWastedTime,
        dailyRateStepsCount
    ) { isTrackingNow, totalStepsCount, totalKilometersCount, totalCaloriesCount, totalWastedTime, dailyRateStepsCount ->
        State(
            isTrackingNow,
            totalStepsCount,
            totalKilometersCount,
            totalCaloriesCount,
            totalWastedTime,
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
            totalWastedTime.value,
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
        val totalWastedTime: Long,
        val dailyRateStepsCount: Int
    )

    internal companion object {
        private const val DAILY_RATE_PEDOMETER = 6000
    }
}