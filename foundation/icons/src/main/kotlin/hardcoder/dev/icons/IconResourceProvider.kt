package hardcoder.dev.icons

interface IconResourceProvider {
    fun getIcons(): List<Icon>
    fun getIcon(id: Int): Icon
}