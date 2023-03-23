package hardcoder.dev.androidApp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources

class IconResolver(private val context: Context) {

    fun toResourceName(iconResId: Int): String? = try {
        context.resources.getResourceEntryName(iconResId)
    } catch (exception: Resources.NotFoundException) {
        null
    }

    @SuppressLint("DiscouragedApi")
    fun toResourceId(iconResourceName: String): Int {
        return context.resources.getIdentifier(
            iconResourceName,
            "drawable",
            context.packageName
        )
    }
}