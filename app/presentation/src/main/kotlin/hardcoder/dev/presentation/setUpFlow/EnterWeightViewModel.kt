package hardcoder.dev.presentation.setUpFlow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.InputController

class EnterWeightViewModel : ViewModel() {

    val weightInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = 0
    )
}