package hardcoder.dev.androidApp.ui.screens.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.uikit.components.dialog.TitleDialog
import hardcoder.dev.uikit.components.text.Label
import hardcoderdev.healther.app.android.app.R

@Composable
fun DeleteTrackDialog(
    dialogOpen: Boolean,
    controller: RequestController,
    onUpdateDialogOpen: (Boolean) -> Unit,
) {
    TitleDialog(
        dialogOpen = dialogOpen,
        onUpdateDialogOpen = onUpdateDialogOpen,
        value = false,
        iconResId = R.drawable.ic_delete,
        titleResId = R.string.deleteDialog_title_text,
        negativeOptionResId = R.string.deleteDialog_negative_option,
        positiveOptionResId = R.string.deleteDialog_positive_option,
        dialogContent = {
            Label(
                text = stringResource(R.string.deleteDialog_trackDeletionDescription_text),
            )
        },
        onSelect = {
            controller.request()
            onUpdateDialogOpen(false)
        },
        onCancel = {
            onUpdateDialogOpen(false)
        },
    )
}

@Preview
@Composable
fun Preview() {
    DeleteTrackDialog(
        onUpdateDialogOpen = {},
        dialogOpen = true,
        controller = RequestController(
            coroutineScope = rememberCoroutineScope(),
            request = {},
        ),
    )
}