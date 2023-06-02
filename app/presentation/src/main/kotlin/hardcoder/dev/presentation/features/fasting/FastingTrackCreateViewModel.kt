package hardcoder.dev.presentation.features.fasting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.controller.requireSelectedItem
import hardcoder.dev.logic.features.fasting.plan.FastingPlanDurationResolver
import hardcoder.dev.logic.features.fasting.plan.FastingPlanProvider
import hardcoder.dev.logic.features.fasting.track.CurrentFastingManager
import kotlinx.datetime.Clock

class FastingTrackCreateViewModel(
    private val currentFastingManager: CurrentFastingManager,
    private val fastingPlanDurationMapper: FastingPlanDurationResolver,
    fastingPlanProvider: FastingPlanProvider
) : ViewModel() {

    val customFastingHoursInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = 0
    )

    val fastingPlanSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = fastingPlanProvider.provideAllPlans()
    )

    val creationController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            currentFastingManager.startFasting(
                startTime = Clock.System.now(),
                fastingPlan = fastingPlanSelectionController.requireSelectedItem(),
                duration = fastingPlanDurationMapper.resolve(
                    fastingPlanSelectionController.requireSelectedItem(),
                    customFastingHoursInputController.state.value.input
                )
            )
        }
    )
}