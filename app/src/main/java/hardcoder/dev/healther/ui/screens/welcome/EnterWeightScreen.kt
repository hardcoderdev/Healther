package hardcoder.dev.healther.ui.screens.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chargemap.compose.numberpicker.NumberPicker
import hardcoder.dev.healther.R
import hardcoder.dev.healther.ui.base.LocalPresentationModule
import hardcoder.dev.healther.ui.base.composables.IconTextButton
import hardcoder.dev.healther.ui.base.composables.ScaffoldWrapper

@Composable
fun EnterWeightScreen(onGoBack: () -> Unit, onGoForward: () -> Unit) {
    ScaffoldWrapper(
        titleResId = R.string.your_weight_label,
        content = { EnterWeightContent(onGoForward = onGoForward) },
        onGoBack = onGoBack
    )
}

@Composable
fun EnterWeightContent(onGoForward: () -> Unit) {
    val presentationModule = LocalPresentationModule.current

    val userViewModel = viewModel {
        presentationModule.createUserViewModel()
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        var pickerValue by remember { mutableStateOf(0) }

        Text(
            text = stringResource(id = R.string.weight_label),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            NumberPicker(
                value = pickerValue,
                range = MINIMUM_WEIGHT..MAXIMUM_WEIGHT,
                dividersColor = MaterialTheme.colorScheme.primary,
                textStyle = MaterialTheme.typography.titleLarge,
                onValueChange = {
                    pickerValue = it
                },
                modifier = Modifier.weight(1.8f)
            )
            Spacer(modifier = Modifier.width(32.dp))
            Image(
                painter = painterResource(id = R.drawable.weight_measurement),
                contentDescription = null,
                modifier = Modifier
                    .weight(1.2f)
                    .size(60.dp)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        IconTextButton(
            iconResourceId = Icons.Default.Done,
            labelResId = R.string.next_label,
            onClick = {
                userViewModel.updateWeight(pickerValue)
                onGoForward()
            }
        )
    }
}

@Preview
@Composable
fun EnterWeightScreenPreview() {
    EnterWeightScreen(onGoBack = {}, onGoForward = {})
}

private const val MINIMUM_WEIGHT = 30
private const val MAXIMUM_WEIGHT = 400