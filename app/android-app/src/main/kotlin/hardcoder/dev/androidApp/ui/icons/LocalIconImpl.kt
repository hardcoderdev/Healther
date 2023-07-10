package hardcoder.dev.androidApp.ui.icons

import hardcoder.dev.logic.icons.LocalIcon

data class LocalIconImpl(
    override val id: Int,
    val resourceId: Int,
) : LocalIcon

val LocalIcon.impl get() = this as LocalIconImpl

val LocalIcon.resourceId get() = impl.resourceId