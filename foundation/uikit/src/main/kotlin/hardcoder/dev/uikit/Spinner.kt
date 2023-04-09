@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package hardcoder.dev.uikit

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

data class SpinnerEntry(
    val index: Int,
    @StringRes val valueResId: Int
)

@Composable
fun FilledSpinner(
    @StringRes titleResId: Int,
    @StringRes promptResId: Int,
    spinnerEntries: List<SpinnerEntry>,
    @StringRes selectedOptionText: Int?,
    expanded: Boolean,
    onUpdateSelectedOption: (Int?) -> Unit,
    onUpdateExpanded: (Boolean) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onUpdateExpanded
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
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onUpdateExpanded(false) }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = promptResId))
                },
                onClick = {
                    onUpdateExpanded(false)
                    onUpdateSelectedOption(null)
                }
            )
            spinnerEntries.forEach { spinnerEntry ->
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = spinnerEntry.valueResId))
                    },
                    onClick = {
                        onUpdateExpanded(false)
                        onUpdateSelectedOption(spinnerEntry.index)
                    }
                )
            }
        }
    }
}