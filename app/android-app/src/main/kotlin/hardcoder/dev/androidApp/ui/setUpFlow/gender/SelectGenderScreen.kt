package hardcoder.dev.androidApp.ui.setUpFlow.gender

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.androidApp.di.LocalUIModule
import hardcoder.dev.healther.R
import hardcoder.dev.logic.hero.gender.Gender
import hardcoder.dev.presentation.setUpFlow.SelectGenderViewModel
import hardcoder.dev.uikit.InteractionType
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.IconTextButton
import hardcoder.dev.uikit.card.Card
import hardcoder.dev.uikit.icons.Image
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Title

@Composable
fun SelectGenderScreen(onGoBack: () -> Unit, onGoForward: (Gender) -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val selectedGenderViewModel = viewModel {
        presentationModule.getSelectGenderViewModel()
    }
    val state = selectedGenderViewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            SelectGenderContent(
                state = state.value,
                onUpdateGender = selectedGenderViewModel::updateGender,
                onGoForward = {
                    onGoForward(state.value.selectedGender)
                }
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.setUpFlow_selectGender_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun SelectGenderContent(
    state: SelectGenderViewModel.State,
    onUpdateGender: (Gender) -> Unit,
    onGoForward: () -> Unit
) {
    val uiModule = LocalUIModule.current
    val genderResourcesProvider = uiModule.genderResourcesProvider

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Column(Modifier.weight(2f)) {
            Title(text = stringResource(id = R.string.setUpFlow_selectGender_selectYourGender_text))
            Spacer(modifier = Modifier.height(32.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                state.availableGenderList.forEach { gender ->
                    Card(
                        interactionType = InteractionType.SELECTION,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .height(230.dp),
                        selectedItem = state.selectedGender,
                        item = gender,
                        onClick = { onUpdateGender(gender) }
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                modifier = Modifier.height(190.dp),
                                imageResId = genderResourcesProvider.provide(gender).imageResId
                            )
                            Title(text = stringResource(id = genderResourcesProvider.provide(gender).nameResId))
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Description(text = stringResource(id = R.string.setUpFlow_selectGender_forWhatGender_text))
        }
        IconTextButton(
            iconResId = R.drawable.ic_done,
            labelResId = R.string.setUpFlow_selectGender_next_buttonText,
            onClick = onGoForward
        )
    }
}