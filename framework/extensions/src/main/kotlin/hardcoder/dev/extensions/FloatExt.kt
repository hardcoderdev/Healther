package hardcoder.dev.extensions

import java.math.RoundingMode
import java.text.DecimalFormat

fun Float.roundOffToThreeDecimal(): Float {
    val decimalFormat = DecimalFormat("#,##").apply { roundingMode = RoundingMode.CEILING }
    return decimalFormat.format(this).toFloat()
}

fun Float.calculateProgress(dailyRate: Float): Float {
    return if (this < 100) {
        (this / dailyRate).toString().substring(0, 4).toFloat()
    } else {
        (this / dailyRate).toString().substring(0, 3).toFloat()
    }
}