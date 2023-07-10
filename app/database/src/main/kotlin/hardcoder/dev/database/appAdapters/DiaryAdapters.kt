package hardcoder.dev.database.appAdapters

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import hardcoder.dev.database.DiaryAttachment
import hardcoder.dev.database.DiaryTag
import hardcoder.dev.database.DiaryTrack
import hardcoder.dev.database.columnAdapters.InstantAdapter

object DiaryAdapters {

    fun createDiaryTrackAdapter() = DiaryTrack.Adapter(
        dateAdapter = InstantAdapter,
        idAdapter = IntColumnAdapter,
    )

    fun createDiaryAttachmentAdapter() = DiaryAttachment.Adapter(
        idAdapter = IntColumnAdapter,
        diaryTrackIdAdapter = IntColumnAdapter,
        targetIdAdapter = IntColumnAdapter,
        targetTypeIdAdapter = IntColumnAdapter,
    )

    fun createDiaryTagAdapter() = DiaryTag.Adapter(
        idAdapter = IntColumnAdapter,
        iconIdAdapter = IntColumnAdapter,
    )
}