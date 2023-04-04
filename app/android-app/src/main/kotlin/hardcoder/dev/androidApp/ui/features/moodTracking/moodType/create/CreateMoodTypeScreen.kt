package hardcoder.dev.androidApp.ui.features.moodTracking.moodType.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.androidApp.ui.icons.IconItem
import hardcoder.dev.healther.R
import hardcoder.dev.logic.icons.LocalIcon
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeCreateViewModel
import hardcoder.dev.uikit.IntSlider
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.IconTextButton
import hardcoder.dev.uikit.icons.Icon
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.FilledTextField
import hardcoder.dev.uikit.text.Title

@Composable
fun CreateMoodTypeScreen(onGoBack: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getMoodTypeTrackCreateViewModel()
    }
    val state = viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.value.creationState) {
        if (state.value.creationState is MoodTypeCreateViewModel.CreationState.Executed) {
            onGoBack()
        }
    }

    ScaffoldWrapper(
        content = {
            CreateMoodTypeContent(
                state = state.value,
                onUpdateName = viewModel::updateName,
                onUpdateIcon = viewModel::updateSelectedIcon,
                onUpdatePositivePercentage = viewModel::updatePositivePercentage,
                onCreateMoodType = viewModel::createMoodType
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_createMoodType_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun CreateMoodTypeContent(
    state: MoodTypeCreateViewModel.State,
    onUpdateName: (String) -> Unit,
    onUpdateIcon: (LocalIcon) -> Unit,
    onUpdatePositivePercentage: (Int) -> Unit,
    onCreateMoodType: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            Modifier
                .weight(2f)
                .verticalScroll(rememberScrollState())
        ) {
            EnterMoodTypeNameSection(state = state, onUpdateName = onUpdateName)
            Spacer(modifier = Modifier.height(32.dp))
            SelectIconSection(state = state, onUpdateIcon = onUpdateIcon)
            Spacer(modifier = Modifier.height(32.dp))
            EnterMoodTypePositivePercentageSection(
                state = state,
                onUpdatePositivePercentage = onUpdatePositivePercentage
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        IconTextButton(
            iconResId = R.drawable.ic_save,
            labelResId = R.string.moodTracking_createMoodType_buttonText,
            onClick = onCreateMoodType,
            isEnabled = state.allowCreation
        )
    }
}

@Composable
private fun EnterMoodTypeNameSection(
    state: MoodTypeCreateViewModel.State,
    onUpdateName: (String) -> Unit
) {
    Title(text = stringResource(id = R.string.moodTracking_createMoodType_enterName_text))
    Spacer(modifier = Modifier.height(16.dp))
    FilledTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.name ?: "",
        onValueChange = onUpdateName,
        label = R.string.moodTracking_createMoodType_enterName_textField,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        leadingIcon = {
            Icon(
                iconResId = R.drawable.ic_description,
                contentDescription = stringResource(
                    id = R.string.waterTracking_createDrinkType_nameIcon_contentDescription
                )
            )
        }
    )
}

@Composable
private fun SelectIconSection(
    state: MoodTypeCreateViewModel.State,
    onUpdateIcon: (LocalIcon) -> Unit
) {
    Title(text = stringResource(id = R.string.moodTracking_createMoodType_selectIcon_text))
    Spacer(modifier = Modifier.height(16.dp))
    LazyHorizontalGrid(
        modifier = Modifier.height(200.dp),
        rows = GridCells.Fixed(count = 3),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(state.availableIconsList) {
            IconItem(
                icon = it,
                contentDescriptionResId = R.string.waterTracking_createDrinkType_drinkTypeIconContentDescription,
                selectedIcon = state.selectedIcon,
                onSelectIcon = onUpdateIcon
            )
        }
    }
}

@Composable
private fun EnterMoodTypePositivePercentageSection(
    state: MoodTypeCreateViewModel.State,
    onUpdatePositivePercentage: (Int) -> Unit
) {
    Title(text = stringResource(id = R.string.moodTracking_createMoodType_selectPositivePercentage_text))
    Spacer(modifier = Modifier.height(16.dp))
    Description(
        text = stringResource(
            id = R.string.moodTracking_createMoodType_selectedIndex_formatText,
            formatArgs = arrayOf(state.positivePercentage)
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
    IntSlider(
        selectedValue = state.positivePercentage,
        onValueChange = onUpdatePositivePercentage,
        valueRange = 10..100
    )
}