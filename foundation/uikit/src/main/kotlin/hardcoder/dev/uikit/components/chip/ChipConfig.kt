package hardcoder.dev.uikit.components.chip

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

sealed class ChipConfig {
    data class Action(
        val modifier: Modifier = Modifier,
        val onClick: () -> Unit,
        val text: String,
        val padding: PaddingValues = PaddingValues(16.dp),
        @DrawableRes val iconResId: Int? = null,
        val overflow: TextOverflow = TextOverflow.Ellipsis,
        val shape: RoundedCornerShape = RoundedCornerShape(bottomEnd = 16.dp),
    ) : ChipConfig()

    data class Selection(
        val modifier: Modifier = Modifier,
        val onSelect: () -> Unit,
        val text: String,
        val isSelected: Boolean = false,
        val padding: PaddingValues = PaddingValues(16.dp),
        @DrawableRes val iconResId: Int? = null,
        val overflow: TextOverflow = TextOverflow.Ellipsis,
        val shape: RoundedCornerShape = RoundedCornerShape(bottomEnd = 16.dp),
    ) : ChipConfig()

    data class Static(
        val modifier: Modifier = Modifier,
        val text: String,
        val padding: PaddingValues = PaddingValues(16.dp),
        @DrawableRes val iconResId: Int? = null,
        val overflow: TextOverflow = TextOverflow.Ellipsis,
        val shape: RoundedCornerShape = RoundedCornerShape(bottomEnd = 16.dp),
    ) : ChipConfig()
}