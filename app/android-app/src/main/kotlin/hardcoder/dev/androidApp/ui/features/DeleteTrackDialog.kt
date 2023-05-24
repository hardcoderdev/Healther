package hardcoder.dev.androidApp.ui.features

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import hardcoder.dev.healther.R
import hardcoder.dev.uikit.buttons.ButtonStyles
import hardcoder.dev.uikit.buttons.SimpleButton
import hardcoder.dev.uikit.icons.Icon
import hardcoder.dev.uikit.text.Label
import hardcoder.dev.uikit.text.Title

@Composable
fun DeleteTrackDialog(
    dialogOpen: Boolean,
    onUpdateDialogOpen: (Boolean) -> Unit,
    onApprove: () -> Unit,
    onCancel: () -> Unit
) {
    if (dialogOpen) {
        Dialog(
            onDismissRequest = { onUpdateDialogOpen(false) },
            properties = DialogProperties(dismissOnClickOutside = false)
        ) {
            Row(
                modifier = Modifier
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(16.dp)),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(34.dp),
                            iconResId = R.drawable.ic_delete,
                            contentDescription = stringResource(id = R.string.deleteDialog_iconContentDescription)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Title(text = stringResource(R.string.deleteDialog_trackDeletionTitle_text))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Label(text = stringResource(R.string.deleteDialog_trackDeletionDescription_text))
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.End) {
                        SimpleButton(
                            modifier = Modifier.weight(1f),
                            labelResId = R.string.deleteDialog_yes_option,
                            onClick = onApprove
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        SimpleButton(
                            modifier = Modifier.weight(1f),
                            labelResId = R.string.deleteDialog_no_option,
                            onClick = onCancel,
                            style = ButtonStyles.OUTLINED
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    DeleteTrackDialog(
        onApprove = {},
        onCancel = {},
        onUpdateDialogOpen = {},
        dialogOpen = true
    )
}