package hardcoder.dev.presentation.features.moodTracking

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.getInput
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.MultiSelectionController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.controller.selection.selectedItemsOrEmptySet
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.toInstant
import hardcoder.dev.logics.features.moodTracking.moodActivity.MoodActivityProvider
import hardcoder.dev.logics.features.moodTracking.moodTrack.MoodTrackCreator
import hardcoder.dev.logics.features.moodTracking.moodType.MoodTypeProvider
import kotlinx.coroutines.flow.first

class MoodTrackingCreationViewModel(
    private val moodTrackCreator: MoodTrackCreator,
    dateTimeProvider: DateTimeProvider,
    moodTypeProvider: MoodTypeProvider,
    moodActivityProvider: MoodActivityProvider,
) : ScreenModel {

    val dateController = InputController(
        coroutineScope = coroutineScope,
        initialInput = dateTimeProvider.currentDate(),
    )

    val timeInputController = InputController(
        coroutineScope = coroutineScope,
        initialInput = dateTimeProvider.currentTime().time,
    )

    val moodTypeSelectionController = SingleSelectionController(
        coroutineScope = coroutineScope,
        itemsFlow = moodTypeProvider.provideAllMoodTypes(),
    )

    val noteInputController = InputController(
        coroutineScope = coroutineScope,
        initialInput = "",
    )

    val activitiesMultiSelectionController = MultiSelectionController(
        coroutineScope = coroutineScope,
        itemsFlow = moodActivityProvider.provideAllActivities(),
    )

    val creationController = RequestController(
        coroutineScope = coroutineScope,
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