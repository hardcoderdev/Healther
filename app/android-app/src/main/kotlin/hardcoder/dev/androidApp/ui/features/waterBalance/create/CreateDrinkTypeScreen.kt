package hardcoder.dev.androidApp.ui.features.waterBalance.create

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.ui.IconItem
import hardcoder.dev.androidApp.ui.LocalIconProvider
import hardcoder.dev.androidApp.ui.LocalPresentationModule
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.waterBalance.CreateDrinkTypeViewModel
import hardcoder.dev.uikit.FilledTextField
import hardcoder.dev.uikit.IconTextButton
import hardcoder.dev.uikit.IntSlider
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.Text
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType

@Composable
fun CreateDrinkTypeScreen(onGoBack: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.createDrinkTypeViewModel()
    }
    val state = viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.value.creationState) {
        if (state.value.creationState is CreateDrinkTypeViewModel.CreationState.Executed) {
            onGoBack()
        }
    }

    ScaffoldWrapper(
        content = {
            CreateDrinkTypeContent(
                state = state.value,
                onUpdateName = viewModel::updateName,
                onUpdateIconResource = viewModel::updateSelectedIconResource,
                onUpdateHydrationIndexPercentage = viewModel::updateHydrationIndexPercentage,
                onCreateDrinkType = viewModel::createDrinkType
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.createDrinkType_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun CreateDrinkTypeContent(
    state: CreateDrinkTypeViewModel.State,
    onUpdateName: (String) -> Unit,
    onUpdateIconResource: (String) -> Unit,
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
                .verticalScroll(rememberScrollState())) {
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
            imageVector = Icons.Filled.Save,
            labelResId = R.string.createDrinkType_createTrack_buttonText,
            onClick = onCreateDrinkType,
            isEnabled = state.allowCreation
        )
    }
}

@Composable
private fun EnterDrinkTypeNameSection(
    state: CreateDrinkTypeViewModel.State,
    onUpdateName: (String) -> Unit
) {
    Text(
        text = stringResource(id = R.string.createDrinkType_enterName_text),
        style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.height(16.dp))
    FilledTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.name ?: "",
        onValueChange = onUpdateName,
        label = R.string.createDrinkType_enterName_textField,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Description,
                contentDescription = stringResource(
                    id = R.string.createDrinkType_nameIcon_contentDescription
                )
            )
        }
    )
}

@Composable
private fun SelectIconSection(
    state: CreateDrinkTypeViewModel.State,
    onUpdateIconResource: (String) -> Unit
) {
    val iconProvider = LocalIconProvider.current

    Text(
        text = stringResource(id = R.string.createDrinkType_selectIcon_text),
        style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.height(16.dp))
    LazyHorizontalGrid(
        modifier = Modifier.height(200.dp),
        rows = GridCells.Fixed(count = 3),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(iconProvider.provideWaterTrackingIcons()) {
            IconItem(
                iconResourceName = it,
                contentDescriptionResId = R.string.createDrinkType_drinkTypeIconContentDesription,
                selectedIconResourceName = state.selectedIconResource ?: "",
                onSelectIcon = onUpdateIconResource
            )
        }
    }
}

@Composable
private fun EnterDrinkHydrationIndexPercentageSection(
    state: CreateDrinkTypeViewModel.State,
    onUpdateHydrationIndex: (Int) -> Unit
) {
    Text(
        text = stringResource(id = R.string.createDrinkType_selectHydrationIndex_text),
        style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        style = MaterialTheme.typography.titleMedium,
        text = stringResource(
            id = R.string.createDrinkType_selectedIndex_formatText,
            formatArgs = arrayOf(state.hydrationIndexPercentage)
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
    IntSlider(
        selectedValue = state.hydrationIndexPercentage,
        onValueChange = onUpdateHydrationIndex,
        valueRange = 30..100,
        sliderColors = SliderDefaults.colors(
            inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}