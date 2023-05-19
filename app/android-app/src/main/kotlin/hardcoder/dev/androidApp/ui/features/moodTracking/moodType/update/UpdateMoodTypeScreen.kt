package hardcoder.dev.androidApp.ui.features.moodTracking.moodType.update

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.androidApp.ui.features.DeleteTrackDialog
import hardcoder.dev.androidApp.ui.icons.IconItem
import hardcoder.dev.healther.R
import hardcoder.dev.logic.icons.LocalIcon
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeUpdateViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
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
fun UpdateMoodTypeScreen(
    moodTypeId: Int,
    onGoBack: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getMoodTypeTrackUpdateViewModel(moodTypeId)
    }
    val state = viewModel.state.collectAsState()

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    DeleteTrackDialog(
        dialogOpen = dialogOpen,
        onUpdateDialogOpen = { dialogOpen = it },
        onCancel = { dialogOpen = false },
        onApprove = {
            viewModel.deleteById()
            dialogOpen = false
        }
    )

    LaunchedEffect(key1 = state.value.updateState) {
        if (state.value.updateState is MoodTypeUpdateViewModel.UpdateState.Executed) {
            onGoBack()
        }
    }

    LaunchedEffect(key1 = state.value.deleteState) {
        if (state.value.deleteState is MoodTypeUpdateViewModel.DeleteState.Executed) {
            onGoBack()
        }
    }

    ScaffoldWrapper(
        content = {
            UpdateMoodTypeContent(
                state = state.value,
                onUpdateName = viewModel::updateName,
                onUpdateIcon = viewModel::updateSelectedIcon,
                onUpdatePositivePercentage = viewModel::updatePositivePercentage,
                onUpdateMoodType = viewModel::updateMoodType
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_updateMoodType_title_topBar,
                onGoBack = onGoBack
            )
        ),
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconResId = R.drawable.ic_delete,
                    onActionClick = { dialogOpen = true }
                )
            )
        )
    )
}

@Composable
private fun UpdateMoodTypeContent(
    state: MoodTypeUpdateViewModel.State,
    onUpdateName: (String) -> Unit,
    onUpdateIcon: (LocalIcon) -> Unit,
    onUpdatePositivePercentage: (Int) -> Unit,
    onUpdateMoodType: () -> Unit
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
            SelectIconSection(state = state, onUpdateIconResource = onUpdateIcon)
            Spacer(modifier = Modifier.height(32.dp))
            EnterMoodTypePositivePercentageSection(
                state = state,
                onUpdatePositivePercentage = onUpdatePositivePercentage
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        IconTextButton(
            iconResId = R.drawable.ic_save,
            labelResId = R.string.moodTracking_updateMoodType_createTrack_buttonText,
            onClick = onUpdateMoodType,
            isEnabled = state.allowUpdate
        )
    }
}

@Composable
private fun EnterMoodTypeNameSection(
    state: MoodTypeUpdateViewModel.State,
    onUpdateName: (String) -> Unit
) {
    Title(text = stringResource(id = R.string.moodTracking_updateMoodType_enterName_text))
    Spacer(modifier = Modifier.height(16.dp))
    FilledTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.name ?: "",
        onValueChange = onUpdateName,
        label = R.string.moodTracking_updateMoodType_enterName_textField,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        leadingIcon = {
            Icon(
                iconResId = R.drawable.ic_description,
                contentDescription = stringResource(
                    id = R.string.waterTracking_updateDrinkType_nameIcon_contentDescription
                )
            )
        }
    )
}

@Composable
private fun SelectIconSection(
    state: MoodTypeUpdateViewModel.State,
    onUpdateIconResource: (LocalIcon) -> Unit
) {
    Title(text = stringResource(id = R.string.moodTracking_updateMoodType_selectMoodIcon_text))
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
                contentDescriptionResId = R.string.waterTracking_updateDrinkType_drinkTypeIconContentDescription,
                selectedIcon = state.selectedIcon,
                onSelectIcon = onUpdateIconResource
            )
        }
    }
}

@Composable
private fun EnterMoodTypePositivePercentageSection(
    state: MoodTypeUpdateViewModel.State,
    onUpdatePositivePercentage: (Int) -> Unit
) {
    Title(text = stringResource(id = R.string.moodTracking_updateMoodType_selectPositivePercentage_text))
    Spacer(modifier = Modifier.height(16.dp))
    Description(
        text = stringResource(
            id = R.string.moodTracking_updateMoodType_selectedPositivePercentage_formatText,
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