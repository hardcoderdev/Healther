package hardcoder.dev.androidApp.ui.features.waterBalance.drinkType.update

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
import hardcoder.dev.androidApp.ui.IconItem
import hardcoder.dev.androidApp.ui.LocalPresentationModule
import hardcoder.dev.androidApp.ui.features.DeleteTrackDialog
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.waterBalance.drinkType.DrinkTypeUpdateViewModel
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
fun UpdateDrinkTypeScreen(
    drinkTypeId: Int,
    onGoBack: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getDrinkTypeUpdateViewModel(drinkTypeId)
    }
    val state = viewModel.state.collectAsState()

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    DeleteTrackDialog(
        dialogOpen = dialogOpen,
        onUpdateDialogOpen = { dialogOpen = it },
        onApprove = viewModel::deleteById,
        onCancel = { dialogOpen = false }
    )

    LaunchedEffect(key1 = state.value.updateState) {
        if (state.value.updateState is DrinkTypeUpdateViewModel.UpdateState.Executed) {
            onGoBack()
        }
    }

    LaunchedEffect(key1 = state.value.deleteState) {
        if (state.value.deleteState is DrinkTypeUpdateViewModel.DeleteState.Executed) {
            onGoBack()
        }
    }

    ScaffoldWrapper(
        content = {
            UpdateDrinkTypeContent(
                state = state.value,
                onUpdateName = viewModel::updateName,
                onUpdateIconResource = viewModel::updateSelectedIconResource,
                onUpdateHydrationIndexPercentage = viewModel::updateHydrationIndexPercentage,
                onUpdateDrinkType = viewModel::updateDrinkType
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.updateDrinkType_title_topBar,
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
private fun UpdateDrinkTypeContent(
    state: DrinkTypeUpdateViewModel.State,
    onUpdateName: (String) -> Unit,
    onUpdateIconResource: (String) -> Unit,
    onUpdateHydrationIndexPercentage: (Int) -> Unit,
    onUpdateDrinkType: () -> Unit
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
            EnterDrinkTypeNameSection(state = state, onUpdateName = onUpdateName)
            Spacer(modifier = Modifier.height(32.dp))
            SelectIconSection(state = state, onUpdateIconResource = onUpdateIconResource)
            Spacer(modifier = Modifier.height(32.dp))
            EnterDrinkHydrationIndexPercentageSection(
                state = state,
                onUpdateHydrationIndex = onUpdateHydrationIndexPercentage
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        IconTextButton(
            iconResId = R.drawable.ic_save,
            labelResId = R.string.updateDrinkType_createTrack_buttonText,
            onClick = onUpdateDrinkType,
            isEnabled = state.allowUpdate
        )
    }
}

@Composable
private fun EnterDrinkTypeNameSection(
    state: DrinkTypeUpdateViewModel.State,
    onUpdateName: (String) -> Unit
) {
    Title(text = stringResource(id = R.string.updateDrinkType_enterName_text))
    Spacer(modifier = Modifier.height(16.dp))
    FilledTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.name ?: "",
        onValueChange = onUpdateName,
        label = R.string.updateDrinkType_enterName_textField,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        leadingIcon = {
            Icon(
                iconResId = R.drawable.ic_description,
                contentDescription = stringResource(
                    id = R.string.updateDrinkType_nameIcon_contentDescription
                )
            )
        }
    )
}

@Composable
private fun SelectIconSection(
    state: DrinkTypeUpdateViewModel.State,
    onUpdateIconResource: (String) -> Unit
) {
    Title(text = stringResource(id = R.string.updateDrinkType_selectIcon_text))
    Spacer(modifier = Modifier.height(16.dp))
    LazyHorizontalGrid(
        modifier = Modifier.height(200.dp),
        rows = GridCells.Fixed(count = 3),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(state.availableIconResourceList) {
            IconItem(
                iconResourceName = it,
                contentDescriptionResId = R.string.updateDrinkType_drinkTypeIconContentDescription,
                selectedIconResourceName = state.selectedIconResource ?: "",
                onSelectIcon = onUpdateIconResource
            )
        }
    }
}

@Composable
private fun EnterDrinkHydrationIndexPercentageSection(
    state: DrinkTypeUpdateViewModel.State,
    onUpdateHydrationIndex: (Int) -> Unit
) {
    Title(text = stringResource(id = R.string.updateDrinkType_selectHydrationIndex_text))
    Spacer(modifier = Modifier.height(16.dp))
    Description(
        text = stringResource(
            id = R.string.updateDrinkType_selectedIndex_formatText,
            formatArgs = arrayOf(state.hydrationIndexPercentage)
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
    IntSlider(
        selectedValue = state.hydrationIndexPercentage,
        onValueChange = onUpdateHydrationIndex,
        valueRange = 30..100
    )
}