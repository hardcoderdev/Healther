package hardcoder.dev.logic.features.diary

class AttachmentTypeIdMapper {

    private val map = mapOf(
        AttachmentType.FASTING_ENTITY to FASTING_ENTITY_ATTACHMENT_TYPE_ID,
        AttachmentType.MOOD_TRACKING_ENTITY to MOOD_TRACKING_ENTITY_ATTACHMENT_TYPE_ID,
        AttachmentType.TAG to TAG_ATTACHMENT_TYPE_ID,
    )

    fun mapToAttachmentType(id: Int) = checkNotNull(
        map.entries.find { it.value == id },
    ).key

    fun mapToId(attachmentType: AttachmentType): Int {
        return checkNotNull(map[attachmentType])
    }

    private companion object {
        private const val FASTING_ENTITY_ATTACHMENT_TYPE_ID = 0
        private const val MOOD_TRACKING_ENTITY_ATTACHMENT_TYPE_ID = 1
        private const val TAG_ATTACHMENT_TYPE_ID = 2
    }
}