package hardcoder.dev.logic.icons

interface IconResourceProvider {
    fun getIcons(): List<LocalIcon>
    fun getIcon(id: Long): LocalIcon
}