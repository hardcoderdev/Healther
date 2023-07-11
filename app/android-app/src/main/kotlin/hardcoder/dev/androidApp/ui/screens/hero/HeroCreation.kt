package hardcoder.dev.androidApp.ui.screens.hero

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.request.SingleRequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.logic.hero.IncorrectHeroName
import hardcoder.dev.logic.hero.ValidatedHeroName
import hardcoder.dev.logic.hero.gender.Gender
import hardcoder.dev.presentation.hero.HeroCreationViewModel
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.container.SingleCardSelectionRow
import hardcoder.dev.uikit.components.icon.Icon
import hardcoder.dev.uikit.components.icon.Image
import hardcoder.dev.uikit.components.picker.NumberInput
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.text.textField.TextFieldValidationAdapter
import hardcoder.dev.uikit.components.text.textField.TextInputAdapter
import hardcoder.dev.uikit.components.text.textField.ValidatedTextField
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoderdev.healther.app.android.app.R
import org.koin.compose.koinInject

private const val MINIMUM_WEIGHT = 30
private const val MAXIMUM_WEIGHT = 400
private const val MINIMUM_STRESS_TIME_HOURS = 0
private const val MAXIMUM_STRESS_TIME_HOURS = 24

@Composable
fun HeroCreation(
    viewModel: HeroCreationViewModel,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            HeroCreationContent(
                heroCreationController = viewModel.heroCreationController,
                genderSelectionController = viewModel.genderSelectionController,
                nameInputController = viewModel.nameInputController,
                weightInputController = viewModel.weightInputController,
                exerciseStressTimeInputController = viewModel.exerciseStressTimeInputController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.hero_creation_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun HeroCreationContent(
    heroCreationController: SingleRequestController,
    genderSelectionController: SingleSelectionController<Gender>,
    nameInputController: ValidatedInputController<String, ValidatedHeroName>,
    weightInputController: InputController<Int>,
    exerciseStressTimeInputController: InputController<Int>,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
    ) {
        Column(
            Modifier
                .weight(2f)
                .verticalScroll(rememberScrollState()),
        ) {
            SelectGenderSection(genderSelectionController = genderSelectionController)
            Spacer(modifier = Modifier.height(16.dp))
            EnterNameSection(context = context, nameInputController = nameInputController)
            Spacer(modifier = Modifier.height(16.dp))
            EnterWeight(weightInputController = weightInputController)
            Spacer(modifier = Modifier.height(16.dp))
            EnterExerciseStress(exerciseStressTimeInputController = exerciseStressTimeInputController)
        }
        RequestButtonWithIcon(
            requestButtonConfig = RequestButtonConfig.Filled(
                controller = heroCreationController,
                iconResId = R.drawable.ic_done,
                labelResId = R.string.hero_creation_create_buttonText,
            ),
        )
    }
}

@Composable
private fun SelectGenderSection(genderSelectionController: SingleSelectionController<Gender>) {
    val genderResourcesProvider = koinInject<GenderResourcesProvider>()

    Title(text = stringResource(id = R.string.hero_creation_selectYourGender_text))
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
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier.height(200.dp),
                    imageResId = itemResources.imageResId,
                )
                Title(
                    text = stringResource(itemResources.nameResId),
                )
            }
        },
    )

    Spacer(modifier = Modifier.height(32.dp))
    Description(text = stringResource(id = R.string.hero_creation_forWhatWeNeedGender_text))
}

@Composable
private fun EnterNameSection(
    context: Context,
    nameInputController: ValidatedInputController<String, ValidatedHeroName>,
) {
    Title(text = stringResource(id = R.string.hero_creation_enterName_text))
    Spacer(modifier = Modifier.height(16.dp))
    ValidatedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = R.string.hero_creation_enterName_textField,
        controller = nameInputController,
        inputAdapter = TextInputAdapter,
        validationAdapter = TextFieldValidationAdapter {
            if (it !is IncorrectHeroName) {
                null
            } else {
                when (val reason = it.reason) {
                    is IncorrectHeroName.Reason.Empty -> {
                        context.getString(R.string.hero_creation_nameEmpty)
                    }

                    is IncorrectHeroName.Reason.MoreThanMaxChars -> {
                        context.getString(
                            R.string.hero_creation_nameMoreThanMaxCharsError,
                            reason.maxChars,
                        )
                    }
                }
            }
        },
        leadingIcon = {
            Icon(
                iconResId = R.drawable.ic_description,
                contentDescription = stringResource(
                    id = R.string.hero_creation_nameIcon_contentDescription,
                ),
            )
        },
    )
}

@Composable
private fun EnterWeight(weightInputController: InputController<Int>) {
    Title(text = stringResource(id = R.string.hero_creation_enterYourWeightInKg_text))
    Spacer(modifier = Modifier.height(32.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        NumberInput(
            modifier = Modifier.weight(1.8f),
            controller = weightInputController,
            range = MINIMUM_WEIGHT..MAXIMUM_WEIGHT,
        )
        Spacer(modifier = Modifier.width(32.dp))
        Image(
            imageResId = R.drawable.weight_measurement,
            contentDescription = null,
            modifier = Modifier
                .weight(1.2f)
                .size(60.dp),
        )
    }
}

@Composable
private fun EnterExerciseStress(exerciseStressTimeInputController: InputController<Int>) {
    Title(text = stringResource(id = R.string.hero_creation_enterExerciseStressTime_text))
    Spacer(modifier = Modifier.height(32.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        NumberInput(
            modifier = Modifier.weight(1.8f),
            controller = exerciseStressTimeInputController,
            range = MINIMUM_STRESS_TIME_HOURS..MAXIMUM_STRESS_TIME_HOURS,
        )
        Spacer(modifier = Modifier.width(32.dp))
        Image(
            imageResId = R.drawable.exercise_stress_time,
            contentDescription = null,
            modifier = Modifier
                .weight(1.2f)
                .size(60.dp),
        )
    }
}