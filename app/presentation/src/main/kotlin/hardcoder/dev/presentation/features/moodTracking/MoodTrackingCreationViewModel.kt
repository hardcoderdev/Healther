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
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.toInstant
import hardcoder.dev.logic.features.moodTracking.moodActivity.MoodActivityProvider
import hardcoder.dev.logic.features.moodTracking.moodTrack.MoodTrackCreator
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypeProvider
import kotlinx.coroutines.flow.first

class MoodTrackingCreationViewModel(
    private val moodTrackCreator: MoodTrackCreator,
    dateTimeProvider: DateTimeProvider,
    moodTypeProvider: MoodTypeProvider,
    moodActivityProvider: MoodActivityProvider,
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

    val creationController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            moodTrackCreator.create(
                note = noteInputController.state.value.input.ifEmpty { null },
                moodType = moodTypeSelectionController.requireSelectedItem(),
                date = dateController.getInput().toInstant(timeInputController.getInput()),
                selectedActivities = activitiesMultiSelectionController.selectedItemsOrEmptySet().first(),
            )
        },
    )
}