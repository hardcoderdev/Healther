package hardcoder.dev.logic.dashboard.features

sealed class DateRangeFilterType {
    object ByAllPeriod : DateRangeFilterType()
    object ByDay : DateRangeFilterType()
    object ByWeek : DateRangeFilterType()
    object ByMonth : DateRangeFilterType()
    object ByYear : DateRangeFilterType()
}