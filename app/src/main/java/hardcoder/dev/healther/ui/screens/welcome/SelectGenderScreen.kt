package hardcoder.dev.healther.ui.screens.welcome

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.healther.R
import hardcoder.dev.healther.ui.base.LocalPresentationModule
import hardcoder.dev.healther.ui.base.composables.IconTextButton
import hardcoder.dev.healther.ui.base.composables.ScaffoldWrapper

enum class Gender { MALE, FEMALE }

@Composable
fun SelectGenderScreen(onGoBack: () -> Unit, onGoForward: () -> Unit) {
    ScaffoldWrapper(
        titleResId = R.string.gender_title,
        content = { SelectGenderContent(onGoForward = onGoForward) },
        onGoBack = onGoBack
    )
}

@Composable
fun SelectGenderContent(onGoForward: () -> Unit) {
    val presentationModule = LocalPresentationModule.current

    val userViewModel = viewModel {
        presentationModule.createUserViewModel()
    }

    var selectedGender by remember {
        mutableStateOf(Gender.MALE)
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.gender_label),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .height(200.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                border = if (selectedGender == Gender.MALE) BorderStroke(
                    width = 3.dp,
                    color = MaterialTheme.colorScheme.primary
                ) else null
            ) {
                IconButton(
                    onClick = { selectedGender = Gender.MALE },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.male),
                        contentDescription = ""
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .height(200.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                border = if (selectedGender == Gender.FEMALE) BorderStroke(
                    width = 3.dp,
                    color = MaterialTheme.colorScheme.primary
                ) else null
            ) {
                IconButton(
                    onClick = { selectedGender = Gender.FEMALE },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.female),
                        contentDescription = ""
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.gender_description),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(32.dp))
        IconTextButton(
            iconResourceId = Icons.Default.Done,
            labelResId = R.string.next_label,
            onClick = {
                userViewModel.updateGender(selectedGender)
                onGoForward()
            }
        )
    }
}

@Preview
@Composable
fun SelectGenderScreenPreview() {
    SelectGenderScreen(onGoBack = {}, onGoForward = {})
}