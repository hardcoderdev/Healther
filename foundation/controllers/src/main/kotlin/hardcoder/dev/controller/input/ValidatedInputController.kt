package hardcoder.dev.controller.input

import hardcoder.dev.controller.StateController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

interface InputController<INPUT> : StateController<InputController.State<INPUT>> {

    fun changeInput(input: INPUT)

    interface State<INPUT> {
        val input: INPUT
    }
}

fun <INPUT> InputController(
    coroutineScope: CoroutineScope,
    initialInput: INPUT,
): InputController<INPUT> = InputControllerImpl(
    coroutineScope,
    initialInput,
)

private class InputControllerImpl<INPUT>(
    coroutineScope: CoroutineScope,
    initialInput: INPUT,
) : InputController<INPUT> {
    private val inputState = MutableStateFlow(initialInput)

    override fun changeInput(input: INPUT) {
        inputState.value = input
    }

    override val state = inputState.map(::State).stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            input = inputState.value,
        ),
    )

    data class State<INPUT>(
        override val input: INPUT,
    ) : InputController.State<INPUT>
}

class ValidatedInputController<INPUT, VALIDATION_RESULT>(
    private val coroutineScope: CoroutineScope,
    initialInput: INPUT,
    private val validation: suspend INPUT.() -> VALIDATION_RESULT?,
) : InputController<INPUT> {
    private val inputState = MutableStateFlow(initialInput)
    private val validationResultState = MutableStateFlow<VALIDATION_RESULT?>(null)

    override val state = combine(inputState, validationResultState, ::State).stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            input = inputState.value,
            validationResult = validationResultState.value,
        ),
    )

    suspend fun validateAndAwait() = inputState.value.validation().also {
        validationResultState.value = it
    }

    fun validate() {
        coroutineScope.launch {
            validateAndAwait()
        }
    }

    override fun changeInput(input: INPUT) {
        inputState.value = input
        validate()
    }

    data class State<INPUT, VALIDATION_RESULT>(
        override val input: INPUT,
        val validationResult: VALIDATION_RESULT,
    ) : InputController.State<INPUT>
}

fun <INPUT> InputController<INPUT>.getInput() = state.value.input

suspend inline fun <reified RESULT> ValidatedInputController<*, *>.validateAndRequire() =
    validateAndAwait() as RESULT