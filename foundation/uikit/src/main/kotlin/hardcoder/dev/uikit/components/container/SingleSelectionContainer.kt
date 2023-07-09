package hardcoder.dev.uikit.components.container

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.uikit.components.card.internal.SelectionCard

@Composable
fun <T> SingleCardSelectionRow(
    controller: SingleSelectionController<T>,
    itemContent: @Composable (item: T, isSelected: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    itemModifier: RowScope.() -> Modifier = { Modifier },
    loadingContent: @Composable RowScope.() -> Unit = { DefaultLoadingContent() },
    emptyContent: @Composable RowScope.() -> Unit = { DefaultEmptyContent() },
) {
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
    ) {
        when (val state = controller.state.collectAsState().value) {
            is SingleSelectionController.State.Loaded -> {
                state.items.forEach { item ->
                    val isSelected = state.selectedItem == item

                    SelectionCard(
                        modifier = itemModifier(),
                        isSelected = isSelected,
                        onSelect = { controller.select(item) },
                    ) {
                        itemContent(item, isSelected)
                    }
                }
            }

            SingleSelectionController.State.Empty -> emptyContent()
            SingleSelectionController.State.Loading -> loadingContent()
        }
    }
}

@Composable
fun <T> SingleCardSelectionHorizontalGrid(
    controller: SingleSelectionController<T>,
    itemContent: @Composable (item: T, isSelected: Boolean) -> Unit,
    rows: GridCells,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal = if (!reverseLayout) Arrangement.Start else Arrangement.End,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    itemModifier: LazyGridItemScope.() -> Modifier = { Modifier },
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() },
    emptyContent: @Composable () -> Unit = { DefaultEmptyContent() },
) {
    when (val state = controller.state.collectAsState().value) {
        is SingleSelectionController.State.Loaded -> {
            LazyHorizontalGrid(
                modifier = modifier,
                rows = rows,
                state = gridState,
                contentPadding = contentPadding,
                reverseLayout = reverseLayout,
                horizontalArrangement = horizontalArrangement,
                verticalArrangement = verticalArrangement,
                flingBehavior = flingBehavior,
                userScrollEnabled = userScrollEnabled,
            ) {
                items(state.items) { item ->
                    val isSelected = state.selectedItem == item

                    SelectionCard(
                        modifier = itemModifier(),
                        isSelected = isSelected,
                        onSelect = { controller.select(item) },
                    ) {
                        itemContent(item, isSelected)
                    }
                }
            }
        }

        SingleSelectionController.State.Empty -> emptyContent()
        SingleSelectionController.State.Loading -> loadingContent()
    }
}

@Composable
fun <T> SingleCardSelectionVerticalGrid(
    controller: SingleSelectionController<T>,
    itemContent: @Composable (item: T, isSelected: Boolean) -> Unit,
    columns: GridCells,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal = if (!reverseLayout) Arrangement.Start else Arrangement.End,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    itemModifier: LazyGridItemScope.() -> Modifier = { Modifier },
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() },
    emptyContent: @Composable () -> Unit = { DefaultEmptyContent() },
) {
    when (val state = controller.state.collectAsState().value) {
        is SingleSelectionController.State.Loaded -> {
            LazyVerticalGrid(
                modifier = modifier,
                columns = columns,
                state = gridState,
                contentPadding = contentPadding,
                reverseLayout = reverseLayout,
                horizontalArrangement = horizontalArrangement,
                verticalArrangement = verticalArrangement,
                flingBehavior = flingBehavior,
                userScrollEnabled = userScrollEnabled,
            ) {
                items(state.items) { item ->
                    val isSelected = state.selectedItem == item

                    SelectionCard(
                        modifier = itemModifier(),
                        isSelected = isSelected,
                        onSelect = { controller.select(item) },
                    ) {
                        itemContent(item, isSelected)
                    }
                }
            }
        }
        is SingleSelectionController.State.Loading -> loadingContent()
        is SingleSelectionController.State.Empty -> emptyContent()
    }
}

@Composable
private fun DefaultEmptyContent() {
    // TODO yes
}

@Composable
private fun DefaultLoadingContent() {
    // TODO aga
}