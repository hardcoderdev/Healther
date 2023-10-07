package hardcoder.dev.androidApp.ui.screens.hero.creation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hardcoder.dev.androidApp.ui.screens.hero.GenderResourcesProvider
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.logic.hero.IncorrectHeroHeroExerciseStress
import hardcoder.dev.logic.hero.IncorrectHeroName
import hardcoder.dev.logic.hero.IncorrectHeroWeight
import hardcoder.dev.logic.hero.ValidatedHeroExerciseStress
import hardcoder.dev.logic.hero.ValidatedHeroName
import hardcoder.dev.logic.hero.ValidatedHeroWeight
import hardcoder.dev.logic.hero.gender.Gender
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.container.SingleCardSelectionRow
import hardcoder.dev.uikit.components.icon.Icon
import hardcoder.dev.uikit.components.icon.Image
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.text.textField.TextFieldValidationAdapter
import hardcoder.dev.uikit.components.text.textField.TextInputAdapter
import hardcoder.dev.uikit.components.text.textField.ValidatedTextField
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.resources.R

@Composable
fun HeroCreation(
    genderResourcesProvider: GenderResourcesProvider,
    heroCreationController: RequestController,
    genderSelectionController: SingleSelectionController<Gender>,
    nameInputController: ValidatedInputController<String, ValidatedHeroName>,
    weightInputController: ValidatedInputController<String, ValidatedHeroWeight>,
    exerciseStressTimeInputController: ValidatedInputController<String, ValidatedHeroExerciseStress>,
) {
    ScaffoldWrapper(
        content = {
            HeroCreationContent(
                genderResourcesProvider = genderResourcesProvider,
                heroCreationController = heroCreationController,
                genderSelectionController = genderSelectionController,
                nameInputController = nameInputController,
                weightInputController = weightInputController,
                exerciseStressTimeInputController = exerciseStressTimeInputController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TitleTopBar(
                titleResId = R.string.hero_creation_title_topBar,
            ),
        ),
    )
}

@Composable
private fun HeroCreationContent(
    genderResourcesProvider: GenderResourcesProvider,
    heroCreationController: RequestController,
    genderSelectionController: SingleSelectionController<Gender>,
    nameInputController: ValidatedInputController<String, ValidatedHeroName>,
    weightInputController: ValidatedInputController<String, ValidatedHeroWeight>,
    exerciseStressTimeInputController: ValidatedInputController<String, ValidatedHeroExerciseStress>,
) {
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
            SelectGenderSection(
                genderResourcesProvider = genderResourcesProvider,
                genderSelectionController = genderSelectionController,
            )
            Spacer(modifier = Modifier.height(16.dp))
            EnterNameSection(nameInputController = nameInputController)
            Spacer(modifier = Modifier.height(16.dp))
            EnterWeight(weightInputController = weightInputController)
            Spacer(modifier = Modifier.height(16.dp))
            EnterExerciseStress(exerciseStressTimeInputController = exerciseStressTimeInputController)
        }
        Spacer(modifier = Modifier.height(16.dp))
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
private fun SelectGenderSection(
    genderResourcesProvider: GenderResourcesProvider,
    genderSelectionController: SingleSelectionController<Gender>,
) {
    SingleCardSelectionRow(
        controller = genderSelectionController,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        itemModifier = {
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .height(240.dp)
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
}

@Composable
private fun EnterNameSection(
    nameInputController: ValidatedInputController<String, ValidatedHeroName>,
) {
    val context = LocalContext.current

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
                        context.getString(R.string.errors_fieldCantBeEmptyError)
                    }

                    is IncorrectHeroName.Reason.MoreThanMaxChars -> {
                        context.getString(
                            R.string.errors_moreThanMaxCharsError,
                            reason.maxChars,
                        )
                    }
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
        ),
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
private fun EnterWeight(
    weightInputController: ValidatedInputController<String, ValidatedHeroWeight>,
) {
    val context = LocalContext.current

    ValidatedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = R.string.hero_creation_enterYourWeightInKg_textField,
        controller = weightInputController,
        inputAdapter = TextInputAdapter,
        validationAdapter = TextFieldValidationAdapter {
            if (it !is IncorrectHeroWeight) {
                null
            } else {
                when (val reason = it.reason) {
                    is IncorrectHeroWeight.Reason.Empty -> {
                        context.getString(
                            R.string.errors_fieldCantBeEmptyError,
                        )
                    }

                    is IncorrectHeroWeight.Reason.MoreThanMaximum -> {
                        context.getString(
                            R.string.hero_creation_weightMoreThanMaximumBoundaryError,
                            reason.maximumBoundary,
                        )
                    }

                    is IncorrectHeroWeight.Reason.LessThanMinimum -> {
                        context.getString(
                            R.string.hero_creation_weightLessThanMinimumBoundaryError,
                            reason.minimumBoundary,
                        )
                    }

                    is IncorrectHeroWeight.Reason.InvalidCharsInWeight -> {
                        context.getString(
                            R.string.errors_invalidCharsError,
                            reason.invalidChars,
                        )
                    }
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Decimal,
        ),
        leadingIcon = {
            Icon(
                iconResId = R.drawable.ic_weight,
                contentDescription = stringResource(
                    id = R.string.hero_creation_weightIcon_contentDescription,
                ),
            )
        },
    )
}

@Composable
private fun EnterExerciseStress(
    exerciseStressTimeInputController: ValidatedInputController<String, ValidatedHeroExerciseStress>,
) {
    val context = LocalContext.current

    ValidatedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = R.string.hero_creation_enterExerciseStressTime_textField,
        controller = exerciseStressTimeInputController,
        inputAdapter = TextInputAdapter,
        validationAdapter = TextFieldValidationAdapter {
            if (it !is IncorrectHeroHeroExerciseStress) {
                null
            } else {
                when (val reason = it.reason) {
                    is IncorrectHeroHeroExerciseStress.Reason.Empty -> {
                        context.getString(
                            R.string.errors_fieldCantBeEmptyError,
                        )
                    }

                    is IncorrectHeroHeroExerciseStress.Reason.MoreThanMaximum -> {
                        context.getString(
                            R.string.hero_creation_exerciseStressTimeMoreThanMaximumBoundaryError,
                            reason.maximumBoundary,
                        )
                    }

                    is IncorrectHeroHeroExerciseStress.Reason.LessThanMinimum -> {
                        context.getString(
                            R.string.hero_creation_exerciseStressTimeLessThanMinimumBoundaryError,
                            reason.minimumBoundary,
                        )
                    }

                    is IncorrectHeroHeroExerciseStress.Reason.InvalidCharsInWeight -> {
                        context.getString(
                            R.string.errors_invalidCharsError,
                            reason.invalidChars.joinToString(separator = " "),
                        )
                    }
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Decimal,
        ),
        leadingIcon = {
            Icon(
                iconResId = R.drawable.ic_time,
                contentDescription = stringResource(
                    id = R.string.hero_creation_exerciseStressTimeIcon_contentDescription,
                ),
            )
        },
    )
}

@HealtherScreenPhonePreviews
@Composable
private fun HeroCreationPreview() {
    HealtherTheme {
        HeroCreation(
            genderResourcesProvider = GenderResourcesProvider(),
            nameInputController = MockControllersProvider.validatedInputController(""),
            heroCreationController = MockControllersProvider.requestController(),
            exerciseStressTimeInputController = MockControllersProvider.validatedInputController(""),
            weightInputController = MockControllersProvider.validatedInputController(""),
            genderSelectionController = MockControllersProvider.singleSelectionController(
                dataList = Gender.entries,
            ),
        )
    }
}