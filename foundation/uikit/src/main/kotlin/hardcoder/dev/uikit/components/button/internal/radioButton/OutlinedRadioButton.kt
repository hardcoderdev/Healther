package hardcoder.dev.uikit.components.button.internal.radioButton

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.components.text.Text
import hardcoder.dev.uikit.preview.elements.HealtherUiKitPreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.foundation.uikit.R

@Composable
internal fun OutlinedRadioButton(
    modifier: Modifier = Modifier,
    @StringRes labelResId: Int,
    onClick: () -> Unit,
    isSelected: Boolean,
    isEnabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = Color.Transparent,
            )
            .border(
                width = 3.dp,
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.onBackground,
            )
            .padding(12.dp)
            .selectable(
                selected = isSelected,
                enabled = isEnabled,
                role = Role.RadioButton,
                onClick = onClick,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            enabled = isEnabled,
            selected = isSelected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.onBackground,
                unselectedColor = MaterialTheme.colorScheme.onBackground,
            ),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = stringResource(id = labelResId),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@HealtherUiKitPreview
@Composable
internal fun OutlinedRadioButtonPreview() {
    HealtherThemePreview {
        OutlinedRadioButton(
            labelResId = R.string.placeholder_label,
            onClick = { /* no-op */ },
            isSelected = true,
        )
    }
}