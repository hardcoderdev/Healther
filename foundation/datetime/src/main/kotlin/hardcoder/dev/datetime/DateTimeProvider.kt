package hardcoder.dev.datetime

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DateTimeProvider(private val dispatchers: BackgroundCoroutineDispatchers) {

    private fun <T> updatingFlow(value: () -> T) = flow {
        while (currentCoroutineContext().isActive) {
            emit(value())
            delay(UPDATE_DELAY)
        }
    }.distinctUntilChanged().flowOn(dispatchers.default)

    fun currentDateRange() = dateRange(currentDate(), currentDate())

    fun dateRange(startDate: LocalDate, endDate: LocalDate): ClosedRange<Instant> {
        return startDate.getStartOfDay(getCurrentTimezone())..endDate.getEndOfDay(getCurrentTimezone())
    }

    fun currentTimeFlow(): Flow<LocalDateTime> = updatingFlow(::currentTime)

    fun currentDateFlow(): Flow<LocalDate> = updatingFlow(::currentDate)

    fun currentDate() = currentInstant().toLocalDateTime(
        timeZone = getCurrentTimezone(),
    ).date

    fun currentTime() = currentInstant().toLocalDateTime(
        timeZone = getCurrentTimezone(),
    )

    fun currentInstant() = Clock.System.now()

    private fun getCurrentTimezone() = TimeZone.currentSystemDefault()

    private companion object {
        private val UPDATE_DELAY = 1.seconds
    }
}