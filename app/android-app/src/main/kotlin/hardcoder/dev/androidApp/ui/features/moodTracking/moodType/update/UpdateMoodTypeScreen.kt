package hardcoder.dev.androidApp.ui.features.moodTracking.moodType.update

import android.content.Context
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import hardcoder.dev.logic.features.moodTracking.moodType.IncorrectMoodTypeName
import hardcoder.dev.logic.features.moodTracking.moodType.ValidatedMoodTypeName
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
fun UpdateMoodTypeScreen(
    moodTypeId: Int,
    onGoBack: () -> Unit
) {
    val context = LocalContext.current
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel { presentationModule.getMoodTypeTrackUpdateViewModel(moodTypeId) }

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    DeleteTrackDialog(
        dialogOpen = dialogOpen,
        onUpdateDialogOpen = { dialogOpen = it },
        onCancel = { dialogOpen = false },
        onApprove = {
            viewModel.deleteController::request
            dialogOpen = false
        }
    )

    LaunchedEffectWhenExecuted(controller = viewModel.updateController, action = onGoBack)
    LaunchedEffectWhenExecuted(controller = viewModel.deleteController, action = onGoBack)

    ScaffoldWrapper(
        content = {
            UpdateMoodTypeContent(
                context = context,
                moodTypeNameController = viewModel.moodTypeNameController,
                iconSelectionController = viewModel.iconSelectionController,
                positiveIndexController = viewModel.positiveIndexController,
                updateController = viewModel.updateController
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
    context: Context,
    moodTypeNameController: ValidatedInputController<String, ValidatedMoodTypeName>,
    iconSelectionController: SingleSelectionController<LocalIcon>,
    positiveIndexController: InputController<Int>,
    updateController: SingleRequestController
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
            EnterMoodTypeNameSection(
                context = context,
                moodTypeNameController = moodTypeNameController
            )
            Spacer(modifier = Modifier.height(32.dp))
            SelectIconSection(iconSelectionController = iconSelectionController)
            Spacer(modifier = Modifier.height(32.dp))
            EnterMoodTypePositivePercentageSection(positiveIndexController = positiveIndexController)
        }
        Spacer(modifier = Modifier.height(16.dp))
        RequestButtonWithIcon(
            controller = updateController,
            iconResId = R.drawable.ic_save,
            labelResId = R.string.moodTracking_updateMoodType_createTrack_buttonText
        )
    }
}

@Composable
private fun EnterMoodTypeNameSection(
    context: Context,
    moodTypeNameController: ValidatedInputController<String, ValidatedMoodTypeName>
) {
    Title(text = stringResource(id = R.string.moodTracking_updateMoodType_enterName_text))
    Spacer(modifier = Modifier.height(16.dp))
    ValidatedTextField(
        controller = moodTypeNameController,
        modifier = Modifier.fillMaxWidth(),
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
        },
        validationAdapter = rememberValidationAdapter {
            if (it !is IncorrectMoodTypeName) {
                null
            } else when (val reason = it.reason) {
                is IncorrectMoodTypeName.Reason.Empty -> {
                    context.getString(R.string.moodTracking_updateMoodType_nameEmpty_error)
                }

                is IncorrectMoodTypeName.Reason.MoreThanMaxChars -> {
                    context.getString(
                        R.string.moodTracking_updateMoodType_nameMoreThanMaxChars_error,
                        reason.maxChars
                    )
                }
            }
        }
    )
}

@Composable
private fun SelectIconSection(iconSelectionController: SingleSelectionController<LocalIcon>) {
    Title(text = stringResource(id = R.string.moodTracking_updateMoodType_selectMoodIcon_text))
    Spacer(modifier = Modifier.height(16.dp))
    SingleCardSelectionHorizontalGrid(
        controller = iconSelectionController,
        modifier = Modifier.height(200.dp),
        rows = GridCells.Fixed(count = 3),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp),
        itemContent = { icon, _ ->
            Icon(
                iconResId = icon.resourceId,
                contentDescription = stringResource(id = R.string.waterTracking_updateDrinkType_drinkTypeIconContentDescription),
                modifier = Modifier
                    .size(60.dp)
                    .padding(12.dp)
            )
        }
    )
}

@Composable
private fun EnterMoodTypePositivePercentageSection(positiveIndexController: InputController<Int>) {
    val state by positiveIndexController.state.collectAsState()

    Title(text = stringResource(id = R.string.moodTracking_updateMoodType_selectPositivePercentage_text))
    Spacer(modifier = Modifier.height(16.dp))
    Description(
        text = stringResource(
            id = R.string.moodTracking_updateMoodType_selectedPositivePercentage_formatText,
            formatArgs = arrayOf(state.input)
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
    IntSlider(
        selectedValue = state.input,
        onValueChange = positiveIndexController::changeInput,
        valueRange = 10..100
    )
}