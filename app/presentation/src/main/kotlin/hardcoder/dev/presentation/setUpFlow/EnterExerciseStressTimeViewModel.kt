package hardcoder.dev.presentation.setUpFlow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.InputController

class EnterExerciseStressTimeViewModel : ViewModel() {

    val exerciseStressTimeInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = 0
    )

}