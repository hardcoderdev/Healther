package hardcoder.dev.logic.dashboard.features.diary

class AttachmentTypeIdMapper {

    private val map = mapOf(
        AttachmentType.FASTING_ENTITY to 0,
        AttachmentType.MOOD_TRACKING_ENTITY to 1,
        AttachmentType.TAG to 2
    )

    fun mapToType(id: Int): AttachmentType {
        return map.entries.find { it.value == id }!!.key
    }

    fun mapToId(attachmentType: AttachmentType): Int {
        return checkNotNull(map[attachmentType])
    }
}