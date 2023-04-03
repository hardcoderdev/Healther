package hardcoder.dev.androidApp.ui.features.waterTracking.drinkType.create

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
import hardcoder.dev.presentation.features.waterTracking.drinkType.DrinkTypeCreateViewModel
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
fun CreateDrinkTypeScreen(onGoBack: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getDrinkTypeCreateViewModel()
    }
    val state = viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.value.creationState) {
        if (state.value.creationState is DrinkTypeCreateViewModel.CreationState.Executed) {
            onGoBack()
        }
    }

    ScaffoldWrapper(
        content = {
            CreateDrinkTypeContent(
                state = state.value,
                onUpdateName = viewModel::updateName,
                onUpdateIcon = viewModel::updateSelectedIcon,
                onUpdateHydrationIndexPercentage = viewModel::updateHydrationIndexPercentage,
                onCreateDrinkType = viewModel::createDrinkType
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.waterTracking_CreateDrinkType_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun CreateDrinkTypeContent(
    state: DrinkTypeCreateViewModel.State,
    onUpdateName: (String) -> Unit,
    onUpdateIcon: (LocalIcon) -> Unit,
    onUpdateHydrationIndexPercentage: (Int) -> Unit,
    onCreateDrinkType: () -> Unit
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
            SelectIconSection(state = state, onUpdateIcon = onUpdateIcon)
            Spacer(modifier = Modifier.height(32.dp))
            EnterDrinkHydrationIndexPercentageSection(
                state = state,
                onUpdateHydrationIndex = onUpdateHydrationIndexPercentage
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        IconTextButton(
            iconResId = R.drawable.ic_save,
            labelResId = R.string.waterTracking_CreateDrinkType_createTrack_buttonText,
            onClick = onCreateDrinkType,
            isEnabled = state.allowCreation
        )
    }
}

@Composable
private fun EnterDrinkTypeNameSection(
    state: DrinkTypeCreateViewModel.State,
    onUpdateName: (String) -> Unit
) {
    Title(text = stringResource(id = R.string.waterTracking_CreateDrinkType_enterName_text))
    Spacer(modifier = Modifier.height(16.dp))
    FilledTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.name ?: "",
        onValueChange = onUpdateName,
        label = R.string.waterTracking_CreateDrinkType_enterName_textField,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        leadingIcon = {
            Icon(
                iconResId = R.drawable.ic_description,
                contentDescription = stringResource(
                    id = R.string.waterTracking_CreateDrinkType_nameIcon_contentDescription
                )
            )
        }
    )
}

@Composable
private fun SelectIconSection(
    state: DrinkTypeCreateViewModel.State,
    onUpdateIcon: (LocalIcon) -> Unit
) {
    Title(text = stringResource(id = R.string.waterTracking_CreateDrinkType_selectIcon_text))
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
                contentDescriptionResId = R.string.waterTracking_CreateDrinkType_drinkTypeIconContentDescription,
                selectedIcon = state.selectedIcon,
                onSelectIcon = onUpdateIcon
            )
        }
    }
}

@Composable
private fun EnterDrinkHydrationIndexPercentageSection(
    state: DrinkTypeCreateViewModel.State,
    onUpdateHydrationIndex: (Int) -> Unit
) {
    Title(text = stringResource(id = R.string.waterTracking_CreateDrinkType_selectHydrationIndex_text))
    Spacer(modifier = Modifier.height(16.dp))
    Description(
        text = stringResource(
            id = R.string.waterTracking_CreateDrinkType_selectedIndex_formatText,
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