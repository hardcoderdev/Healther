package hardcoder.dev.uikit.lists

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import hardcoder.dev.controller.SingleSelectionController

@Composable
fun <T> SingleSelectionScrollableTabRow(
    controller: SingleSelectionController<T>,
    actionButton: @Composable () -> Unit = {},
    itemContent: @Composable (items: List<T>, selectedItem: T) -> Unit,
    emptyContent: @Composable () -> Unit = { DefaultEmptyContent() },
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() }
) {
    when (val state = controller.state.collectAsState().value) {
        is SingleSelectionController.State.Loaded -> {
            ScrollableTabRow(selectedTabIndex = state.items.indexOf(state.selectedItem) + 1) {
                actionButton()
                itemContent(
                    items = state.items,
                    selectedItem = state.selectedItem
                )
            }
        }

        is SingleSelectionController.State.Loading -> {
            loadingContent()
        }

        is SingleSelectionController.State.Empty -> {
            emptyContent()
        }
    }
}

@Composable
private fun DefaultEmptyContent() {
    // TODO EMPTY CONTENT
}

@Composable
private fun DefaultLoadingContent() {
    // TODO LOADING CONTENT
}
