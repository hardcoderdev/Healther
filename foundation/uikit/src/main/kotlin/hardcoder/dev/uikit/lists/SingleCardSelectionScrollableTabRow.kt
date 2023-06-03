package hardcoder.dev.uikit.lists

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.uikit.card.SelectionCard

@Composable
fun <T> SingleCardSelectionScrollableTabRow(
    controller: SingleSelectionController<T>,
    itemModifier: () -> Modifier = { Modifier },
    actionButton: @Composable () -> Unit = {},
    itemContent: @Composable (item: T, isSelected: Boolean) -> Unit,
    emptyContent: @Composable () -> Unit = { DefaultEmptyContent() },
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() }
) {
    when (val state = controller.state.collectAsState().value) {
        is SingleSelectionController.State.Loaded -> {
            ScrollableTabRow(selectedTabIndex = state.items.indexOf(state.selectedItem) + 1) {
                actionButton()
                state.items.forEach { item ->
                    val isSelected = state.selectedItem == item
                    SelectionCard(
                        modifier = itemModifier(),
                        isSelected = isSelected,
                        onSelect = { controller.select(item) }
                    ) {
                        itemContent(item, isSelected)
                    }
                }
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
