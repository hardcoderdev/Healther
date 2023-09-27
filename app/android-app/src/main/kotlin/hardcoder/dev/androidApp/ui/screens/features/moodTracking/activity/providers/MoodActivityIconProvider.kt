package hardcoder.dev.androidApp.ui.screens.features.moodTracking.activity.providers

import hardcoder.dev.icons.IconImpl
import hardcoder.dev.icons.IconResourceProvider
import hardcoder.dev.icons.Icon
import hardcoderdev.healther.app.resources.R

class MoodActivityIconProvider : IconResourceProvider {

    private val icons = listOf(
        create(0, R.drawable.ic_mma),
        create(1, R.drawable.ic_tennis),
        create(2, R.drawable.ic_soccer),
        create(3, R.drawable.ic_baseball),
        create(4, R.drawable.ic_volleyball),
        create(5, R.drawable.ic_basketball),
        create(6, R.drawable.ic_golf),
        create(7, R.drawable.ic_gymnastics),
        create(8, R.drawable.ic_martial_arts),
        create(9, R.drawable.ic_esports),
        create(10, R.drawable.ic_handball),
        create(11, R.drawable.ic_hockey),
        create(12, R.drawable.ic_roller_skating),
        create(13, R.drawable.ic_rugby),
        create(14, R.drawable.ic_motorsports),
        create(15, R.drawable.ic_snowboarding),
    )

    override fun getIcons(): List<Icon> = icons

    override fun getIcon(id: Int): Icon = icons.first { it.id == id }

    private fun create(id: Int, resourceId: Int) = IconImpl(id, resourceId)
}