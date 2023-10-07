package hardcoder.dev.mock.dataProviders.features

import android.content.Context
import hardcoder.dev.entities.features.diary.DiaryTag
import hardcoder.dev.entities.features.diary.DiaryTrack
import hardcoder.dev.mock.dataProviders.IconsMockDataProvider
import hardcoder.dev.mock.dataProviders.date.MockDateProvider
import hardcoder.dev.presentation.features.diary.DiaryUpdateViewModel
import hardcoderdev.healther.app.ui.resources.R

object DiaryMockDataProvider {

    fun diaryTracksList(context: Context) = listOf(
        DiaryTrack(
            id = 0,
            content = context.getString(R.string.diary_nowEmpty_text),
            date = MockDateProvider.instant(),
            diaryAttachmentGroup = null,
        ),
        DiaryTrack(
            id = 1,
            content = context.getString(R.string.diary_nowEmpty_text),
            date = MockDateProvider.instant(),
            diaryAttachmentGroup = diaryAttachmentsMoodTrack(context, false).toDiaryAttachmentGroup(),
        ),
        DiaryTrack(
            id = 2,
            content = context.getString(R.string.diary_nowEmpty_text),
            date = MockDateProvider.instant(),
            diaryAttachmentGroup = null,
        ),
        DiaryTrack(
            id = 3,
            content = context.getString(R.string.diary_nowEmpty_text),
            date = MockDateProvider.instant(),
            diaryAttachmentGroup = diaryAttachmentsMoodTrack(context, false).toDiaryAttachmentGroup(),
        ),
    )

    fun diaryTagsList(context: Context): List<DiaryTag> {
        val iconsList = IconsMockDataProvider.icons()

        return listOf(
            DiaryTag(
                id = 0,
                name = context.getString(R.string.predefined_drinkType_name_soda),
                icon = iconsList[0],
            ),
            DiaryTag(
                id = 1,
                name = context.getString(R.string.predefined_drinkType_name_beer),
                icon = iconsList[1],
            ),
            DiaryTag(
                id = 2,
                name = context.getString(R.string.predefined_drinkType_name_soup),
                icon = iconsList[2],
            ),
            DiaryTag(
                id = 3,
                name = context.getString(R.string.predefined_drinkType_name_water),
                icon = iconsList[3],
            ),
            DiaryTag(
                id = 4,
                name = context.getString(R.string.predefined_drinkType_name_juice),
                icon = iconsList[4],
            ),
            DiaryTag(
                id = 5,
                name = context.getString(R.string.predefined_drinkType_name_coffee),
                icon = iconsList[5],
            ),
        )
    }

    fun diaryAttachmentsMoodTrack(
        context: Context,
        isWithTags: Boolean = true,
    ) = DiaryUpdateViewModel.ReadOnlyDiaryAttachments(
        fastingTracks = emptyList(),
        moodTracks = MoodTrackingMockDataProvider.moodTracksList(context),
        tags = if (isWithTags) diaryTagsList(context).toSet() else emptySet(),
    )

    private fun DiaryUpdateViewModel.ReadOnlyDiaryAttachments.toDiaryAttachmentGroup() =
        hardcoder.dev.entities.features.diary.DiaryAttachmentGroup(
            fastingTracks = fastingTracks,
            moodTracks = moodTracks,
            tags = tags,
        )
}