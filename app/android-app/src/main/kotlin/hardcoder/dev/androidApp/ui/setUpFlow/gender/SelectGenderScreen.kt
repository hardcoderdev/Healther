package hardcoder.dev.androidApp.ui.setUpFlow.gender

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.controller.requireSelectedItem
import hardcoder.dev.logic.hero.gender.Gender
import hardcoder.dev.presentation.setUpFlow.SelectGenderViewModel
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.SingleCardSelectionRow
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.ButtonWithIcon
import hardcoder.dev.uikit.icons.Image
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Title
import hardcoderdev.healther.app.android.app.R
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun SelectGenderScreen(
    onGoBack: () -> Unit,
    onGoForward: (Gender) -> Unit
) {
    val viewModel = koinViewModel<SelectGenderViewModel>()

    ScaffoldWrapper(
        content = {
            SelectGenderContent(
                genderSelectionController = viewModel.genderSelectionController,
                onGoForward = {
                    onGoForward(viewModel.genderSelectionController.requireSelectedItem())
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
    onGoForward: () -> Unit,
    genderSelectionController: SingleSelectionController<Gender>
) {
    val genderResourcesProvider = koinInject<GenderResourcesProvider>()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Column(Modifier.weight(2f)) {
            Title(text = stringResource(id = R.string.setUpFlow_selectGender_selectYourGender_text))
            Spacer(modifier = Modifier.height(32.dp))
            SingleCardSelectionRow(
                controller = genderSelectionController,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                itemModifier = {
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .height(230.dp)
                },
                itemContent = { item, _ ->
                    val itemResources = genderResourcesProvider.provide(item)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier.height(190.dp),
                            imageResId = itemResources.imageResId
                        )
                        Title(
                            text = stringResource(itemResources.nameResId)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
            Description(text = stringResource(id = R.string.setUpFlow_selectGender_forWhatGender_text))
        }
        ButtonWithIcon(
            iconResId = R.drawable.ic_done,
            labelResId = R.string.setUpFlow_selectGender_next_buttonText,
            onClick = onGoForward
        )
    }
}