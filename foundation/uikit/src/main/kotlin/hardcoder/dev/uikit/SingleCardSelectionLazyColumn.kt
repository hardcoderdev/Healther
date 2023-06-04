package hardcoder.dev.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.uikit.card.SelectionCard

@Composable
fun <T> SingleCardSelectionLazyColumn(
    controller: SingleSelectionController<T>,
    modifier: Modifier = Modifier,
    itemModifier: LazyItemScope.() -> Modifier = { Modifier },
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.dp),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    itemContent: @Composable (item: T, isSelected: Boolean) -> Unit,
    emptyContent: @Composable () -> Unit = { DefaultEmptyContent() },
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() }
) {
    when (val state = controller.state.collectAsState().value) {
        is SingleSelectionController.State.Loaded -> {
            LazyColumn(
                modifier = modifier,
                contentPadding = contentPadding,
                verticalArrangement = verticalArrangement
            ) {
                items(state.items) { item ->
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
    // TODO yes
}

@Composable
private fun DefaultLoadingContent() {
    // TODO aga
}