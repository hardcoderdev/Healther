package hardcoder.dev.screens.user.creation

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
import hardcoder.dev.blocks.components.containers.ScaffoldWrapper
import hardcoder.dev.blocks.components.icon.Icon
import hardcoder.dev.blocks.components.icon.Image
import hardcoder.dev.blocks.components.text.Title
import hardcoder.dev.blocks.components.topBar.TopBarConfig
import hardcoder.dev.blocks.components.topBar.TopBarType
import hardcoder.dev.controller.input.ValidatedInputController
import hardcoder.dev.controller.request.RequestController
import hardcoder.dev.controller.selection.SingleSelectionController
import hardcoder.dev.entities.user.Gender
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.resources.user.GenderResourcesProvider
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.container.SingleCardSelectionRow
import hardcoder.dev.uikit.components.text.textField.TextFieldValidationAdapter
import hardcoder.dev.uikit.components.text.textField.TextInputAdapter
import hardcoder.dev.uikit.components.text.textField.ValidatedTextField
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoder.dev.validators.user.IncorrectUserExerciseStressTime
import hardcoder.dev.validators.user.IncorrectUserWeight
import hardcoder.dev.validators.user.ValidatedUserExerciseStressTime
import hardcoder.dev.validators.user.ValidatedUserName
import hardcoder.dev.validators.user.ValidatedUserWeight
import hardcoderdev.healther.app.ui.resources.R

@Composable
fun UserCreation(
    genderResourcesProvider: GenderResourcesProvider,
    userCreationController: RequestController,
    genderSelectionController: SingleSelectionController<Gender>,
    nameInputController: ValidatedInputController<String, ValidatedUserName>,
    weightInputController: ValidatedInputController<String, ValidatedUserWeight>,
    exerciseStressTimeInputController: ValidatedInputController<String, ValidatedUserExerciseStressTime>,
) {
    ScaffoldWrapper(
        content = {
            UserCreationContent(
                genderResourcesProvider = genderResourcesProvider,
                heroCreationController = userCreationController,
                genderSelectionController = genderSelectionController,
                nameInputController = nameInputController,
                weightInputController = weightInputController,
                exerciseStressTimeInputController = exerciseStressTimeInputController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TitleTopBar(
                titleResId = R.string.user_creation_title_topBar,
            ),
        ),
    )
}

@Composable
private fun UserCreationContent(
    genderResourcesProvider: GenderResourcesProvider,
    heroCreationController: RequestController,
    genderSelectionController: SingleSelectionController<Gender>,
    nameInputController: ValidatedInputController<String, ValidatedUserName>,
    weightInputController: ValidatedInputController<String, ValidatedUserWeight>,
    exerciseStressTimeInputController: ValidatedInputController<String, ValidatedUserExerciseStressTime>,
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
                labelResId = R.string.user_creation_create_buttonText,
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
    nameInputController: ValidatedInputController<String, ValidatedUserName>,
) {
    val context = LocalContext.current

    ValidatedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = R.string.user_creation_enterName_textField,
        controller = nameInputController,
        inputAdapter = TextInputAdapter,
        validationAdapter = TextFieldValidationAdapter {
            if (it !is hardcoder.dev.validators.user.IncorrectUserName) {
                null
            } else {
                when (val reason = it.reason) {
                    is hardcoder.dev.validators.user.IncorrectUserName.Reason.Empty -> {
                        context.getString(R.string.errors_fieldCantBeEmptyError)
                    }

                    is hardcoder.dev.validators.user.IncorrectUserName.Reason.MoreThanMaxChars -> {
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
                    id = R.string.user_creation_nameIcon_contentDescription,
                ),
            )
        },
    )
}

@Composable
private fun EnterWeight(
    weightInputController: ValidatedInputController<String, ValidatedUserWeight>,
) {
    val context = LocalContext.current

    ValidatedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = R.string.user_creation_enterYourWeightInKg_textField,
        controller = weightInputController,
        inputAdapter = TextInputAdapter,
        validationAdapter = TextFieldValidationAdapter {
            if (it !is IncorrectUserWeight) {
                null
            } else {
                when (val reason = it.reason) {
                    is IncorrectUserWeight.Reason.Empty -> {
                        context.getString(
                            R.string.errors_fieldCantBeEmptyError,
                        )
                    }

                    is IncorrectUserWeight.Reason.MoreThanMaximum -> {
                        context.getString(
                            R.string.user_creation_weightMoreThanMaximumBoundaryError,
                            reason.maximumBoundary,
                        )
                    }

                    is IncorrectUserWeight.Reason.LessThanMinimum -> {
                        context.getString(
                            R.string.user_creation_weightLessThanMinimumBoundaryError,
                            reason.minimumBoundary,
                        )
                    }

                    is IncorrectUserWeight.Reason.InvalidCharsInWeight -> {
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
                    id = R.string.user_creation_weightIcon_contentDescription,
                ),
            )
        },
    )
}

@Composable
private fun EnterExerciseStress(
    exerciseStressTimeInputController: ValidatedInputController<String, ValidatedUserExerciseStressTime>,
) {
    val context = LocalContext.current

    ValidatedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = R.string.user_creation_enterExerciseStressTime_textField,
        controller = exerciseStressTimeInputController,
        inputAdapter = TextInputAdapter,
        validationAdapter = TextFieldValidationAdapter {
            if (it !is IncorrectUserExerciseStressTime) {
                null
            } else {
                when (val reason = it.reason) {
                    is IncorrectUserExerciseStressTime.Reason.Empty -> {
                        context.getString(
                            R.string.errors_fieldCantBeEmptyError,
                        )
                    }

                    is IncorrectUserExerciseStressTime.Reason.MoreThanMaximum -> {
                        context.getString(
                            R.string.user_creation_exerciseStressTimeMoreThanMaximumBoundaryError,
                            reason.maximumBoundary,
                        )
                    }

                    is IncorrectUserExerciseStressTime.Reason.LessThanMinimum -> {
                        context.getString(
                            R.string.user_creation_exerciseStressTimeLessThanMinimumBoundaryError,
                            reason.minimumBoundary,
                        )
                    }

                    is IncorrectUserExerciseStressTime.Reason.InvalidCharsInWeight -> {
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
                    id = R.string.user_creation_exerciseStressTimeIcon_contentDescription,
                ),
            )
        },
    )
}

@HealtherScreenPhonePreviews
@Composable
private fun UserCreationPreview() {
    HealtherTheme {
        UserCreation(
            genderResourcesProvider = GenderResourcesProvider(),
            nameInputController = MockControllersProvider.validatedInputController(""),
            userCreationController = MockControllersProvider.requestController(),
            exerciseStressTimeInputController = MockControllersProvider.validatedInputController(""),
            weightInputController = MockControllersProvider.validatedInputController(""),
            genderSelectionController = MockControllersProvider.singleSelectionController(
                dataList = Gender.entries,
            ),
        )
    }
}