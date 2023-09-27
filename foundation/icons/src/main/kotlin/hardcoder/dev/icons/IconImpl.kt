package hardcoder.dev.icons

data class IconImpl(
    override val id: Int,
    val resourceId: Int,
) : Icon

val Icon.impl get() = this as IconImpl

val Icon.resourceId get() = impl.resourceId