package hardcoder.dev.mock.dataProviders.date

import hardcoder.dev.datetime.toInstant
import kotlin.time.Duration
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

object MockDateProvider {

    private const val year = 2004
    private const val month = 6
    private const val dayOfMonth = 8
    private const val hour = 12
    private const val minute = 50
    private const val second = 30

    fun instant() = Clock.System.now()

    fun localDate() = LocalDate(year, month, dayOfMonth)

    fun localTime() = LocalTime(hour, minute, second)

    fun duration(): Duration = instant() - instant()

    fun instantRange(): ClosedRange<Instant> {
        return localDate().toInstant(localTime())..localDate().toInstant(localTime())
    }
}