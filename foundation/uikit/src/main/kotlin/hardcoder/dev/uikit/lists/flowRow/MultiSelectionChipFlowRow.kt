package hardcoder.dev.uikit.lists.flowRow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.MultiSelectionController
import hardcoder.dev.controller.toggleItem
import hardcoder.dev.uikit.chip.content.SelectionContentChip

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> MultiSelectionChipFlowRow(
    modifier: Modifier = Modifier,
    controller: MultiSelectionController<T>,
    itemModifier: Modifier = Modifier,
    chipShape: RoundedCornerShape = RoundedCornerShape(32.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    maxItemsInEachRow: Int,
    actionButton: @Composable () -> Unit = {},
    itemContent: @Composable (item: T, isSelected: Boolean) -> Unit,
    emptyContent: @Composable () -> Unit = { DefaultEmptyContent() },
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() }
) {
    when (val state = controller.state.collectAsState().value) {
        is MultiSelectionController.State.Loaded -> {
            FlowRow(
                modifier = modifier,
                horizontalArrangement = horizontalArrangement,
                verticalAlignment = verticalAlignment,
                maxItemsInEachRow = maxItemsInEachRow
            ) {
                actionButton()
                state.items.forEach { item ->
                    SelectionContentChip(
                        modifier = itemModifier,
                        onSelect = { controller.toggleItem(item) },
                        shape = chipShape,
                        isSelected = state.selectedItems.contains(item),
                        chipContent = { itemContent(item, state.selectedItems.contains(item)) }
                    )
                }
            }
        }

        is MultiSelectionController.State.Loading -> {
            loadingContent()
        }

        is MultiSelectionController.State.Empty -> {
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