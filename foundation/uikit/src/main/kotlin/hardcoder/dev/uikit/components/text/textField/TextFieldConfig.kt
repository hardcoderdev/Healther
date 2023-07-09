package hardcoder.dev.uikit.components.text.textField

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation

sealed class TextFieldConfig {
    data class Filled(
        val modifier: Modifier = Modifier,
        val value: String,
        val onValueChange: (String) -> Unit,
        @StringRes val label: Int? = null,
        val textStyle: TextStyle,
        val multiline: Boolean = false,
        val minLines: Int = 1,
        val maxLines: Int = Int.MAX_VALUE,
        val visualTransformation: VisualTransformation = VisualTransformation.None,
        val keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        val keyboardActions: KeyboardActions = KeyboardActions.Default,
        val isError: Boolean = false,
        val readOnly: Boolean = false,
        val leadingIcon: @Composable (() -> Unit)? = null,
        val trailingIcon: @Composable (() -> Unit)? = null,
        val regex: Regex? = null,
        val onFocusChanged: (FocusState) -> Unit = {},
    ) : TextFieldConfig()

    data class Outlined(
        val value: String,
        val onValueChange: (String) -> Unit,
        val modifier: Modifier = Modifier,
        @StringRes val label: Int? = null,
        val textStyle: TextStyle,
        val multiline: Boolean = false,
        val maxLines: Int = Int.MAX_VALUE,
        val visualTransformation: VisualTransformation = VisualTransformation.None,
        val keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        val keyboardActions: KeyboardActions = KeyboardActions.Default,
        val isError: Boolean = false,
        val readOnly: Boolean = false,
        val leadingIcon: @Composable (() -> Unit)? = null,
        val trailingIcon: @Composable (() -> Unit)? = null,
        val regex: Regex? = null,
    ) : TextFieldConfig()
}