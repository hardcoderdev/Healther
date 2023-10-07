package hardcoder.dev.presentation.features.moodTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.getInput
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.MultiSelectionController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.controller.selection.selectedItemsOrEmptySet
import hardcoder.dev.coroutines.firstNotNull
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.toInstant
import hardcoder.dev.datetime.toLocalDateTime
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentProvider
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackProvider
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivityProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackDeleter
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackUpdater
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeProvider
import hardcoder.dev.logic.features.moodTracking.moodWithActivity.MoodWithActivitiesProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MoodTrackingUpdateViewModel(
    private val moodTrackId: Int,
    private val moodTrackUpdater: MoodTrackUpdater,
    private val moodTrackDeleter: MoodTrackDeleter,
    private val diaryTrackProvider: DiaryTrackProvider,
    private val moodTrackProvider: MoodTrackProvider,
    private val diaryAttachmentProvider: DiaryAttachmentProvider,
    dateTimeProvider: DateTimeProvider,
    moodWithActivityProvider: MoodWithActivitiesProvider,
    moodActivityProvider: MoodActivityProvider,
    moodTypeProvider: MoodTypeProvider,
) : ViewModel() {

    val dateController = InputController(
        coroutineScope = viewModelScope,
        initialInput = dateTimeProvider.currentDate(),
    )

    val timeInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = dateTimeProvider.currentTime().time,
    )

    val moodTypeSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = moodTypeProvider.provideAllMoodTypes(),
    )

    val noteInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = "",
    )

    val activitiesMultiSelectionController = MultiSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = moodActivityProvider.provideAllActivities(),
    )

    val updateController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            val selectedActivities = activitiesMultiSelectionController.selectedItemsOrEmptySet()

            moodTrackProvider.provideById(moodTrackId).firstOrNull()?.let {
                val moodTrack = it.copy(
                    moodType = moodTypeSelectionController.requireSelectedItem(),
                    date = dateController.getInput().toInstant(timeInputController.getInput()),
                )

                moodTrackUpdater.update(
                    note = noteInputController.getInput().ifEmpty { null },
                    moodTrack = moodTrack,
                    selectedActivities = selectedActivities.first(),
                )
            }
        },
    )

    val deleteController = RequestController(
        coroutineScope = viewModelScope,
        request = { moodTrackDeleter.deleteById(moodTrackId) },
    )

    init {
        viewModelScope.launch {
            val moodTrack = moodTrackProvider.provideById(moodTrackId).firstNotNull()
            val moodTrackLocalDateTime = moodTrack.date.toLocalDateTime()

            moodTypeSelectionController.select(moodTrack.moodType)
            dateController.changeInput(moodTrackLocalDateTime.date)
            timeInputController.changeInput(moodTrackLocalDateTime.time)
            activitiesMultiSelectionController.toggleItems(
                moodWithActivityProvider.provideActivityListByMoodTrackId(moodTrackId).first(),
            )

            diaryAttachmentProvider.provideAttachmentByEntityId(
                attachmentType = hardcoder.dev.entities.features.diary.AttachmentType.MOOD_TRACKING_ENTITY,
                entityId = moodTrackId,
            ).firstOrNull()?.let { attachment ->
                diaryTrackProvider.provideDiaryTrackById(
                    attachment.diaryTrackId,
                ).firstOrNull().let { diaryTrack ->
                    noteInputController.changeInput(diaryTrack?.content ?: "")
                }
            }
        }
    }
}