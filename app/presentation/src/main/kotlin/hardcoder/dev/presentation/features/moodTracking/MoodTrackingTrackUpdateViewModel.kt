package hardcoder.dev.presentation.features.moodTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.MultiSelectionController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.controller.requireSelectedItem
import hardcoder.dev.controller.requireSelectedItems
import hardcoder.dev.coroutines.firstNotNull
import hardcoder.dev.logic.features.diary.AttachmentType
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentProvider
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackProvider
import hardcoder.dev.logic.features.moodTracking.activity.Activity
import hardcoder.dev.logic.features.moodTracking.activity.ActivityProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackDeleter
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackUpdater
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeProvider
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivitiesProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class MoodTrackingTrackUpdateViewModel(
    private val moodTrackId: Int,
    private val moodTrackUpdater: MoodTrackUpdater,
    private val moodTrackDeleter: MoodTrackDeleter,
    private val diaryTrackProvider: DiaryTrackProvider,
    private val moodTrackProvider: MoodTrackProvider,
    private val diaryAttachmentProvider: DiaryAttachmentProvider,
    moodWithActivityProvider: MoodWithActivitiesProvider,
    activityProvider: ActivityProvider,
    moodTypeProvider: MoodTypeProvider,
) : ViewModel() {

    private val selectedActivities = MutableStateFlow<List<Activity>>(emptyList())

    val dateController = InputController(
        coroutineScope = viewModelScope,
        initialInput = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )

    val moodTypeSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = moodTypeProvider.provideAllMoodTypes()
    )

    val noteInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = ""
    )

    val activitiesMultiSelectionController = MultiSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = activityProvider.provideAllActivities()
    )

    val updateController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            moodTrackProvider.provideById(moodTrackId).firstOrNull()?.let {
                val moodTrack = it.copy(
                    moodType = moodTypeSelectionController.requireSelectedItem(),
                    date = dateController.state.value.input.toInstant(TimeZone.currentSystemDefault())
                )

                moodTrackUpdater.update(
                    note = noteInputController.state.value.input,
                    moodTrack = moodTrack,
                    selectedActivities = activitiesMultiSelectionController.requireSelectedItems()
                )
            }
        }
    )

    val deleteController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = { moodTrackDeleter.deleteById(moodTrackId) }
    )

    init {
        viewModelScope.launch {
            val moodTrack = moodTrackProvider.provideById(moodTrackId).firstNotNull()
            moodTypeSelectionController.select(moodTrack.moodType)
            dateController.changeInput(moodTrack.date.toLocalDateTime(TimeZone.currentSystemDefault()))
            activitiesMultiSelectionController.toggleItems(
                moodWithActivityProvider.provideActivityListByMoodTrackId(moodTrackId).first()
            )

            diaryAttachmentProvider.provideAttachmentByEntityId(
                attachmentType = AttachmentType.MOOD_TRACKING_ENTITY,
                entityId = moodTrackId
            ).firstOrNull()?.let { attachment ->
                diaryTrackProvider.provideDiaryTrackById(
                    attachment.diaryTrackId
                ).firstOrNull().let { diaryTrack ->
                    noteInputController.changeInput(diaryTrack?.content ?: "")
                }
            }
        }
    }
}