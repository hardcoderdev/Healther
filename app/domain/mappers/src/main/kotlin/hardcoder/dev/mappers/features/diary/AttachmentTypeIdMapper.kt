package hardcoder.dev.mappers.features.diary

import hardcoder.dev.entities.features.diary.AttachmentType

class AttachmentTypeIdMapper {

    private val map = mapOf(
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
        private const val MOOD_TRACKING_ENTITY_ATTACHMENT_TYPE_ID = 0
        private const val TAG_ATTACHMENT_TYPE_ID = 1
    }
}