package hardcoder.dev.androidApp.ui.formatters

import java.util.Locale

class DecimalFormatter {

    fun roundAndFormatToString(number: Float) = String.format(
        locale = Locale.getDefault(),
        format = "%.2f",
        number,
    )
}