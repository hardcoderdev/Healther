package hardcoder.dev.androidApp.ui.features.moodTracking.moodType.create

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.icons.resourceId
import hardcoder.dev.controller.InputController
import hardcoder.dev.controller.SingleRequestController
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.controller.ValidatedInputController
import hardcoder.dev.logic.features.moodTracking.moodType.IncorrectMoodTypeName
import hardcoder.dev.logic.features.moodTracking.moodType.ValidatedMoodTypeName
import hardcoder.dev.logic.icons.LocalIcon
import hardcoder.dev.presentation.features.moodTracking.moodType.MoodTypeCreationViewModel
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
import hardcoderdev.healther.app.android.app.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateMoodTypeScreen(onGoBack: () -> Unit) {
    val context = LocalContext.current
    val viewModel = koinViewModel<MoodTypeCreationViewModel>()

    LaunchedEffectWhenExecuted(controller = viewModel.creationController, action = onGoBack)

    ScaffoldWrapper(
        content = {
            CreateMoodTypeContent(
                context = context,
                moodTypeNameController = viewModel.moodTypeNameController,
                iconSelectionController = viewModel.iconSelectionController,
                positiveIndexController = viewModel.positiveIndexController,
                creationController = viewModel.creationController
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
    context: Context,
    moodTypeNameController: ValidatedInputController<String, ValidatedMoodTypeName>,
    iconSelectionController: SingleSelectionController<LocalIcon>,
    positiveIndexController: InputController<Int>,
    creationController: SingleRequestController
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
            controller = creationController,
            iconResId = R.drawable.ic_save,
            labelResId = R.string.moodTracking_createMoodType_buttonText
        )
    }
}

@Composable
private fun EnterMoodTypeNameSection(
    context: Context,
    moodTypeNameController: ValidatedInputController<String, ValidatedMoodTypeName>
) {
    Title(text = stringResource(id = R.string.moodTracking_createMoodType_enterName_text))
    Spacer(modifier = Modifier.height(16.dp))
    ValidatedTextField(
        controller = moodTypeNameController,
        modifier = Modifier.fillMaxWidth(),
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
        },
        validationAdapter = rememberValidationAdapter {
            if (it !is IncorrectMoodTypeName) {
                null
            } else when (val reason = it.reason) {
                is IncorrectMoodTypeName.Reason.Empty -> {
                    context.getString(
                        R.string.moodTracking_createMoodType_nameEmpty_error
                    )
                }

                is IncorrectMoodTypeName.Reason.MoreThanMaxChars -> {
                    context.getString(
                        R.string.moodTracking_createMoodType_nameMoreThanMaxChars_error,
                        reason.maxChars
                    )
                }
            }
        }
    )
}

@Composable
private fun SelectIconSection(iconSelectionController: SingleSelectionController<LocalIcon>) {
    Title(text = stringResource(id = R.string.moodTracking_createMoodType_selectIcon_text))
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
                contentDescription = stringResource(id = R.string.waterTracking_createDrinkType_drinkTypeIconContentDescription),
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

    Title(text = stringResource(id = R.string.moodTracking_createMoodType_selectPositivePercentage_text))
    Spacer(modifier = Modifier.height(16.dp))
    Description(
        text = stringResource(
            id = R.string.moodTracking_createMoodType_selectedIndex_formatText,
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