package hardcoder.dev.androidApp.ui.features.waterTracking.drinkType.update

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.androidApp.ui.features.DeleteTrackDialog
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.controller.ValidatedInputController
import hardcoder.dev.healther.R
import hardcoder.dev.logic.features.waterTracking.drinkType.IncorrectDrinkTypeName
import hardcoder.dev.logic.features.waterTracking.drinkType.ValidatedDrinkTypeName
import hardcoder.dev.logic.icons.LocalIcon
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.IntSlider
import hardcoder.dev.uikit.LaunchedEffectWhenExecuted
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.SingleCardSelectionHorizontalGrid
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.RequestButtonWithIcon
import hardcoder.dev.uikit.icons.Icon
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Title
import hardcoder.dev.uikit.text.ValidatedTextField
import hardcoder.dev.uikit.text.rememberValidationAdapter

@Composable
fun UpdateDrinkTypeScreen(
    drinkTypeId: Int,
    onGoBack: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getDrinkTypeUpdateViewModel(drinkTypeId)
    }

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    DeleteTrackDialog(
        dialogOpen = dialogOpen,
        onUpdateDialogOpen = { dialogOpen = it },
        onCancel = { dialogOpen = false },
        onApprove = {
            viewModel.deletionController.request()
            dialogOpen = false
        }
    )

    LaunchedEffectWhenExecuted(viewModel.deletionController, onGoBack)
    LaunchedEffectWhenExecuted(viewModel.updatingController, onGoBack)

    ScaffoldWrapper(
        content = {
            UpdateDrinkTypeContent(
                viewModel.nameInputController,
                viewModel.iconSelectionController,
                viewModel.waterPercentageInputController,
                viewModel.updatingController
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.waterTracking_updateDrinkType_title_topBar,
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
    nameInputController: ValidatedInputController<String, ValidatedDrinkTypeName>,
    iconSelectionController: SingleSelectionController<LocalIcon>,
    waterPercentageInputController: InputController<Int>,
    updatingController: SingleRequestController
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
            EnterDrinkTypeNameSection(nameInputController)
            Spacer(modifier = Modifier.height(32.dp))
            SelectIconSection(iconSelectionController)
            Spacer(modifier = Modifier.height(32.dp))
            EnterDrinkHydrationIndexPercentageSection(waterPercentageInputController)
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            controller = updatingController,
            iconResId = R.drawable.ic_save,
            labelResId = R.string.waterTracking_updateDrinkType_createTrack_buttonText,
        )
    }
}

@Composable
private fun EnterDrinkTypeNameSection(
    nameInputController: ValidatedInputController<String, ValidatedDrinkTypeName>,
) {
    Title(text = stringResource(id = R.string.waterTracking_updateDrinkType_enterName_text))
    Spacer(modifier = Modifier.height(16.dp))
    ValidatedTextField(
        modifier = Modifier.fillMaxWidth(),
        controller = nameInputController,
        label = R.string.waterTracking_updateDrinkType_enterName_textField,
        validationAdapter = rememberValidationAdapter {
            if (it !is IncorrectDrinkTypeName) null
            else when (val reason = it.reason) {
                IncorrectDrinkTypeName.Reason.Empty -> "empty"
                is IncorrectDrinkTypeName.Reason.MoreThanMaxChars -> "to long ${reason.maxChars}"
            }
        },
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
    iconSelectionController: SingleSelectionController<LocalIcon>,
) {
    Title(text = stringResource(id = R.string.waterTracking_updateDrinkType_selectIcon_text))
    Spacer(modifier = Modifier.height(16.dp))
    SingleCardSelectionHorizontalGrid(
        modifier = Modifier.height(200.dp),
        rows = GridCells.Fixed(count = 3),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp),
        controller = iconSelectionController,
        itemContent = { icon, _ ->
            Icon(
                modifier = Modifier
                    .size(60.dp)
                    .padding(12.dp),
                iconResId = icon.resourceId,
                contentDescription = stringResource(R.string.waterTracking_updateDrinkType_drinkTypeIconContentDescription)
            )
        }
    )
}

@Composable
private fun EnterDrinkHydrationIndexPercentageSection(
    waterPercentageInputController: InputController<Int>,
) {
    val state by waterPercentageInputController.state.collectAsState()

    Title(text = stringResource(id = R.string.waterTracking_updateDrinkType_selectHydrationIndex_text))
    Spacer(modifier = Modifier.height(16.dp))
    Description(
        text = stringResource(
            id = R.string.waterTracking_updateDrinkType_selectedIndex_formatText,
            formatArgs = arrayOf(state.input)
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
    IntSlider(
        controller = waterPercentageInputController,
        valueRange = 30..100
    )
}