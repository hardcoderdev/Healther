package hardcoder.dev.presentation.features.moodTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.MultiSelectionController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.controller.requireSelectedItem
import hardcoder.dev.controller.selectedItemsOrEmptySet
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    private val selectedActivities = MutableStateFlow<List<Activity>>(emptyList())
    private val initialActivities =
        moodWithActivityProvider.provideActivityListByMoodTrackId(moodTrackId).map {
            selectedActivities.value = it
            it
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
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
                    selectedActivities = activitiesMultiSelectionController.selectedItemsOrEmptySet()
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
            selectedActivities.value = initialActivities.value
//            moodWithActivityProvider.provideActivityListByMoodTrackId(moodTrackId).map {
//                selectedActivities.value = it
//                it
//            }
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