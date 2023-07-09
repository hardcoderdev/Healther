package hardcoder.dev.uikit.components.bottomSheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    state: BottomSheetState,
    sheetContent: @Composable ColumnScope.() -> Unit,
    sheetShape: Shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    content: @Composable () -> Unit,
) {
    ModalBottomSheetLayout(
        sheetState = state.materialState,
        modifier = modifier,
        sheetContent = sheetContent,
        sheetShape = sheetShape,
        content = content,
    )
}

@OptIn(ExperimentalMaterialApi::class)
class BottomSheetState(
    internal val materialState: ModalBottomSheetState,
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
        initialValue = if (showed) {
            ModalBottomSheetValue.Expanded
        } else {
            ModalBottomSheetValue.Hidden
        },
        skipHalfExpanded = true,
    )

    return remember(materialState) {
        BottomSheetState(materialState)
    }
}