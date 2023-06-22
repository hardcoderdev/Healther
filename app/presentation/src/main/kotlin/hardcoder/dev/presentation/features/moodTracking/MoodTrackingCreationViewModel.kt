package hardcoder.dev.presentation.features.moodTracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.MultiSelectionController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.controller.requireSelectedItem
import hardcoder.dev.controller.requireSelectedItems
import hardcoder.dev.logic.features.moodTracking.activity.ActivityProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackCreator
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeProvider
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class MoodTrackingCreationViewModel(
    private val moodTrackCreator: MoodTrackCreator,
    moodTypeProvider: MoodTypeProvider,
    activityProvider: ActivityProvider
) : ViewModel() {

    val dateController = InputController(
        coroutineScope = viewModelScope,
        initialInput = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )

    val moodTypeSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = moodTypeProvider.provideAllMoodTypes()
    )

    val noteController = InputController(
        coroutineScope = viewModelScope,
        initialInput = ""
    )

    val activitiesMultiSelectionController = MultiSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = activityProvider.provideAllActivities()
    )

    val creationController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            moodTrackCreator.create(
                note = noteController.state.value.input,
                moodType = moodTypeSelectionController.requireSelectedItem(),
                date = dateController.state.value.input,
                selectedActivities = activitiesMultiSelectionController.requireSelectedItems()
            )
        }
    )
}