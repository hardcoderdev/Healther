package hardcoder.dev.androidApp.ui.screens.hero.creation

import android.content.Context
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
import hardcoder.dev.presentation.hero.HeroCreationViewModel
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
import hardcoderdev.healther.app.android.app.R
import org.koin.compose.koinInject

@Composable
fun HeroCreation(viewModel: HeroCreationViewModel) {
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
            type = TopBarType.TitleTopBar(
                titleResId = R.string.hero_creation_title_topBar,
            ),
        ),
    )
}

@Composable
private fun HeroCreationContent(
    heroCreationController: RequestController,
    genderSelectionController: SingleSelectionController<Gender>,
    nameInputController: ValidatedInputController<String, ValidatedHeroName>,
    weightInputController: ValidatedInputController<String, ValidatedHeroWeight>,
    exerciseStressTimeInputController: ValidatedInputController<String, ValidatedHeroExerciseStress>,
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
            EnterWeight(context = context, weightInputController = weightInputController)
            Spacer(modifier = Modifier.height(16.dp))
            EnterExerciseStress(context = context, exerciseStressTimeInputController = exerciseStressTimeInputController)
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
private fun SelectGenderSection(genderSelectionController: SingleSelectionController<Gender>) {
    val genderResourcesProvider = koinInject<GenderResourcesProvider>()

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
    context: Context,
    nameInputController: ValidatedInputController<String, ValidatedHeroName>,
) {
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
    context: Context,
    weightInputController: ValidatedInputController<String, ValidatedHeroWeight>,
) {
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
                            R.string.hero_creation_weightEmpty,
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
                            R.string.hero_creation_weight_invalidCharsError,
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
    context: Context,
    exerciseStressTimeInputController: ValidatedInputController<String, ValidatedHeroExerciseStress>,
) {
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
                            R.string.hero_creation_exerciseStressTimeEmpty,
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
                            R.string.hero_creation_exerciseStressTime_invalidCharsError,
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