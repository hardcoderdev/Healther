package hardcoder.dev.uikit.components.spinner

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import hardcoder.dev.uikit.preview.UiKitPhonePreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.foundation.uikit.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilledSpinner(
    @StringRes titleResId: Int,
    @StringRes promptResId: Int,
    spinnerEntries: List<SpinnerEntry>,
    @StringRes selectedOptionText: Int?,
    expanded: Boolean,
    onUpdateSelectedOption: (Int?) -> Unit,
    onUpdateExpanded: (Boolean) -> Unit,
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onUpdateExpanded,
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            value = stringResource(id = selectedOptionText ?: promptResId),
            onValueChange = { },
            label = {
                Text(text = stringResource(id = titleResId))
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onUpdateExpanded(false) },
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = promptResId))
                },
                onClick = {
                    onUpdateExpanded(false)
                    onUpdateSelectedOption(null)
                },
            )
            spinnerEntries.forEachIndexed { index, spinnerEntry ->
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = spinnerEntry.valueResId))
                    },
                    onClick = {
                        onUpdateExpanded(false)
                        onUpdateSelectedOption(index)
                    },
                )
            }
        }
    }
}

@UiKitPhonePreview
@Composable
private fun SpinnerPreview() {
    var expanded by remember {
        mutableStateOf(false)
    }
    var selectedOptionText by remember {
        mutableStateOf(R.string.placeholder_label)
    }
    val spinnerEntries = listOf(
        SpinnerEntry(R.string.default_nowEmpty_text),
        SpinnerEntry(R.string.placeholder_label),
        SpinnerEntry(R.string.default_nowEmpty_text),
    )

    HealtherThemePreview {
        FilledSpinner(
            titleResId = R.string.placeholder_label,
            spinnerEntries = spinnerEntries,
            expanded = expanded,
            onUpdateSelectedOption = { index ->
                index?.let {
                    selectedOptionText = spinnerEntries[index].valueResId
                }
            },
            onUpdateExpanded = { isExpanded ->
                expanded = isExpanded
            },
            selectedOptionText = selectedOptionText,
            promptResId = R.string.placeholder_label,
        )
    }
}