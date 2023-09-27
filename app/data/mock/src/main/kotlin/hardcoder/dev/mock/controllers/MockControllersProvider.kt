package hardcoder.dev.mock.controllers

import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.ToggleController
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.MultiSelectionController
import hardcoder.dev.controller.selection.SingleSelectionController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.flowOf

object MockControllersProvider {

    @OptIn(DelicateCoroutinesApi::class)
    private val mockCoroutineScope: CoroutineScope = GlobalScope

    fun requestController(isAllowed: Boolean = true) = RequestController(
        coroutineScope = mockCoroutineScope,
        request = {},
        isAllowedFlow = flowOf(isAllowed),
    )

    fun toggleController(isActive: Boolean = true) = ToggleController(
        coroutineScope = mockCoroutineScope,
        toggle = {},
        isActiveFlow = flowOf(isActive),
    )

    fun <INPUT : Any> inputController(input: INPUT) = InputController(
        coroutineScope = mockCoroutineScope,
        initialInput = input,
    )

    fun <INPUT : Any, RESULT : Any> validatedInputController(input: INPUT) = ValidatedInputController<INPUT, RESULT>(
        coroutineScope = mockCoroutineScope,
        initialInput = input,
        validation = { null },
    )

    fun <DATA : Any?> loadingController(data: DATA) = LoadingController(
        coroutineScope = mockCoroutineScope,
        flow = flowOf(data),
    )

    fun <DATA : Any> singleSelectionController(
        dataList: List<DATA>,
    ) = SingleSelectionController(
        coroutineScope = mockCoroutineScope,
        items = dataList,
    )

    fun <DATA : Any> multiSelectionController(
        dataList: List<DATA>,
    ) = MultiSelectionController(
        coroutineScope = mockCoroutineScope,
        itemsFlow = flowOf(dataList),
    )
}