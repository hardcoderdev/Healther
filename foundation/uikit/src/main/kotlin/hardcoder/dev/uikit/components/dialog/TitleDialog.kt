package hardcoder.dev.uikit.components.dialog

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import hardcoder.dev.uikit.components.button.simpleButton.SimpleButton
import hardcoder.dev.uikit.components.button.simpleButton.SimpleButtonConfig
import hardcoder.dev.uikit.components.icon.Icon
import hardcoder.dev.uikit.components.text.Title

@Composable
fun <T> TitleDialog(
    dialogOpen: Boolean,
    onUpdateDialogOpen: (Boolean) -> Unit,
    dismissOnClickOutside: Boolean = true,
    value: T,
    @DrawableRes iconResId: Int,
    @StringRes titleResId: Int,
    @StringRes negativeOptionResId: Int,
    @StringRes positiveOptionResId: Int,
    dialogContent: @Composable () -> Unit,
    onSelect: (T) -> Unit,
    onCancel: () -> Unit,
) {
    if (dialogOpen) {
        Dialog(
            onDismissRequest = { onUpdateDialogOpen(false) },
            properties = DialogProperties(
                dismissOnClickOutside = dismissOnClickOutside,
            ),
        ) {
            Row(
                modifier = Modifier
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(16.dp)),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    DialogHeader(
                        iconResId = iconResId,
                        titleResId = titleResId,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    dialogContent()
                    Spacer(modifier = Modifier.height(16.dp))
                    DialogControlButtons(
                        positiveOptionResId = positiveOptionResId,
                        negativeOptionResId = negativeOptionResId,
                        onCancel = onCancel,
                        onSelect = {
                            onSelect(value)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun DialogHeader(
    @DrawableRes iconResId: Int,
    @StringRes titleResId: Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(34.dp),
            iconResId = iconResId,
            contentDescription = stringResource(id = titleResId),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Title(text = stringResource(titleResId))
    }
}

@Composable
private fun DialogControlButtons(
    positiveOptionResId: Int,
    negativeOptionResId: Int,
    onCancel: () -> Unit,
    onSelect: () -> Unit,
) {
    Row(horizontalArrangement = Arrangement.End) {
        SimpleButton(
            simpleButtonConfig = SimpleButtonConfig.Outlined(
                modifier = Modifier.weight(1f),
                labelResId = negativeOptionResId,
                onClick = onCancel,
            ),
        )
        Spacer(modifier = Modifier.width(16.dp))
        SimpleButton(
            simpleButtonConfig = SimpleButtonConfig.Filled(
                modifier = Modifier.weight(1f),
                labelResId = positiveOptionResId,
                onClick = onSelect,
            ),
        )
    }
}