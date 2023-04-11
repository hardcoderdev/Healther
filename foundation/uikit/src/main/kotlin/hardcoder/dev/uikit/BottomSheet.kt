package hardcoder.dev.uikit

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    state: BottomSheetState = rememberBottomSheetState(showed = false),
    sheetContent: @Composable ColumnScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    ModalBottomSheetLayout(
        sheetState = state.materialState,
        modifier = modifier,
        sheetContent = sheetContent,
        content = content
    )
}

@OptIn(ExperimentalMaterialApi::class)
class BottomSheetState(
    internal val materialState: ModalBottomSheetState
) {
    suspend fun toggle() {
        if (materialState.isVisible) {
            materialState.hide()
        } else {
            materialState.show()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberBottomSheetState(showed: Boolean): BottomSheetState {
    val materialState = rememberModalBottomSheetState(
        initialValue = if (showed) ModalBottomSheetValue.Expanded
        else ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    return remember(materialState) {
        BottomSheetState(materialState)
    }
}