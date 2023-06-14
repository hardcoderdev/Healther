package hardcoder.dev.androidApp.ui.formatters

import android.content.Context
import hardcoderdev.healther.app.android.app.R

class LiquidFormatter(
    private val context: Context,
    private val defaultAccuracy: Accuracy
) {

    fun formatMillisDistance(
        defaultMilliliters: Int,
        accuracy: Accuracy = defaultAccuracy,
        usePlurals: Boolean = false
    ) = buildString {
        val hectoliters = defaultMilliliters / 100000
        val decaliters = defaultMilliliters % 100000 / 10000
        val liters = defaultMilliliters % 10000 / 1000
        val milliliters = defaultMilliliters % 1000

        val appendHectoliters = hectoliters != 0
        val appendDecaliters = decaliters != 0 && (!appendHectoliters || accuracy.order > 1)
        val appendLiters = liters != 0 && (!appendHectoliters && !appendDecaliters || accuracy.order > 2)
        val appendMilliliters = milliliters != 0 && (!appendHectoliters && !appendDecaliters && !appendLiters || accuracy.order > 3)

        val hectolitersPlurals =
            context.resources.getQuantityString(R.plurals.hectoliters, hectoliters)
        val decalitersPlurals =
            context.resources.getQuantityString(R.plurals.decaliters, decaliters)
        val litersPlurals =
            context.resources.getQuantityString(R.plurals.liters, liters)
        val millilitersPlurals =
            context.resources.getQuantityString(R.plurals.milliliters, milliliters)

        if (appendHectoliters) {
            if (usePlurals) {
                append(hectoliters)
                append(" ")
                append(hectolitersPlurals)
            } else {
                append(context.getString(R.string.liquid_hectoliters, hectoliters))
            }
        }

        if (appendDecaliters) {
            if (appendHectoliters) append(" ")
            if (usePlurals) {
                append(decaliters)
                append(" ")
                append(decalitersPlurals)
            } else {
                append(context.getString(R.string.liquid_decaliters, decaliters))
            }
        }

        if (appendLiters) {
            if (appendDecaliters || appendHectoliters) append(" ")
            if (usePlurals) {
                append(liters)
                append(" ")
                append(litersPlurals)
            } else {
                append(context.getString(R.string.liquid_liters, liters))
            }
        }

        if (appendMilliliters) {
            if (appendLiters || appendDecaliters || appendHectoliters) append(" ")
            if (usePlurals) {
                append(milliliters)
                append(" ")
                append(millilitersPlurals)
            } else {
                append(context.getString(R.string.liquid_milliliters, milliliters))
            }
        }
    }

    enum class Accuracy(val order: Int) {
        HECTOLITERS(1),
        DECALITERS(2),
        LITERS(3),
        MILLILITERS(4)
    }
}