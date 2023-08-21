package hardcoder.dev.presentation.features.fasting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.getInput
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.controller.selection.requireSelectedItem
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logic.features.fasting.plan.FastingPlanDurationResolver
import hardcoder.dev.logic.features.fasting.plan.FastingPlanProvider
import hardcoder.dev.logic.features.fasting.track.CurrentFastingManager

class FastingCreationViewModel(
    private val currentFastingManager: CurrentFastingManager,
    private val fastingPlanDurationMapper: FastingPlanDurationResolver,
    fastingPlanProvider: FastingPlanProvider,
    dateTimeProvider: DateTimeProvider,
) : ViewModel() {

    val customFastingHoursInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = DEFAULT_CUSTOM_FASTING_HOURS,
    )

    val fastingPlanSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = fastingPlanProvider.provideAllPlans(),
    )

    val creationController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            currentFastingManager.startFasting(
                startTime = dateTimeProvider.currentInstant(),
                fastingPlan = fastingPlanSelectionController.requireSelectedItem(),
                duration = fastingPlanDurationMapper.resolve(
                    fastingPlanSelectionController.requireSelectedItem(),
                    customFastingHoursInputController.getInput(),
                ),
            )
        },
    )

    private companion object {
        const val DEFAULT_CUSTOM_FASTING_HOURS = 4
    }
}