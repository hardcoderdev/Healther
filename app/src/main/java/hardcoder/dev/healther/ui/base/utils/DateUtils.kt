package hardcoder.dev.healther.ui.base.utils

import java.time.LocalDate
import java.time.ZoneId

object DateUtils {

    fun getStartOfDay(localDate: LocalDate): Long {
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun getEndOfDay(localDate: LocalDate): Long {
        val local = localDate.plusDays(1)
        return local.atStartOfDay(ZoneId.systemDefault()).toInstant()
            .toEpochMilli()
    }
}