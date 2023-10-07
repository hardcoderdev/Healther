package hardcoder.dev.resources.features.diary

import hardcoder.dev.icons.Icon
import hardcoder.dev.icons.IconImpl
import hardcoder.dev.icons.IconResourceProvider
import hardcoderdev.healther.app.ui.resources.R

class DiaryTagIconProvider : IconResourceProvider {

    private val icons = listOf(
        create(id = 0, resourceId = R.drawable.ic_mma),
        create(id = 1, resourceId = R.drawable.ic_tennis),
        create(id = 2, resourceId = R.drawable.ic_soccer),
        create(id = 3, resourceId = R.drawable.ic_baseball),
        create(id = 4, resourceId = R.drawable.ic_volleyball),
        create(id = 5, resourceId = R.drawable.ic_basketball),
        create(id = 6, resourceId = R.drawable.ic_golf),
        create(id = 7, resourceId = R.drawable.ic_gymnastics),
        create(id = 8, resourceId = R.drawable.ic_martial_arts),
        create(id = 9, resourceId = R.drawable.ic_esports),
        create(id = 10, resourceId = R.drawable.ic_handball),
        create(id = 11, resourceId = R.drawable.ic_hockey),
        create(id = 12, resourceId = R.drawable.ic_roller_skating),
        create(id = 13, resourceId = R.drawable.ic_rugby),
        create(id = 14, resourceId = R.drawable.ic_motorsports),
        create(id = 15, resourceId = R.drawable.ic_snowboarding),
    )

    override fun getIcons(): List<Icon> = icons

    override fun getIcon(id: Int): Icon = icons.first { it.id == id }

    private fun create(id: Int, resourceId: Int) = IconImpl(id, resourceId)
}