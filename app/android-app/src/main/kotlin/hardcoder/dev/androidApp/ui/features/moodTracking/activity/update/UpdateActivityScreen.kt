package hardcoder.dev.androidApp.ui.features.moodTracking.activity.update

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
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
import hardcoder.dev.logic.features.moodTracking.activity.IncorrectActivityName
import hardcoder.dev.logic.icons.LocalIcon
import hardcoder.dev.presentation.features.moodTracking.activity.UpdateActivityViewModel
import hardcoder.dev.uikit.Action
import hardcoder.dev.uikit.ActionConfig
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.ButtonWithIcon
import hardcoder.dev.uikit.text.ErrorText
import hardcoder.dev.uikit.text.FilledTextField
import hardcoder.dev.uikit.text.Title

@Composable
fun UpdateActivityScreen(
    activityId: Int,
    onGoBack: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getUpdateActivityViewModel(activityId)
    }
    val state = viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.value.updateState) {
        if (state.value.updateState is UpdateActivityViewModel.UpdateState.Executed) {
            onGoBack()
        }
    }

    LaunchedEffect(key1 = state.value.deleteState) {
        if (state.value.deleteState is UpdateActivityViewModel.DeleteState.Executed) {
            onGoBack()
        }
    }

    ScaffoldWrapper(
        content = {
            UpdateActivityContent(
                state = state.value,
                onUpdateActivityName = viewModel::updateActivityName,
                onSelectIcon = viewModel::updateIcon,
                onUpdate = viewModel::updateActivity
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_updateActivity_title_topBar,
                onGoBack = onGoBack
            )
        ),
        actionConfig = ActionConfig(
            actions = listOf(
                Action(
                    iconResId = R.drawable.ic_delete,
                    onActionClick = viewModel::deleteById
                )
            )
        )
    )
}

@Composable
fun UpdateActivityContent(
    state: UpdateActivityViewModel.State,
    onUpdateActivityName: (String) -> Unit,
    onSelectIcon: (LocalIcon) -> Unit,
    onUpdate: () -> Unit
) {
    val validatedActivityName = state.validatedName

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(Modifier.weight(2f)) {
            Title(text = stringResource(id = R.string.moodTracking_updateActivity_enter_name_text))
            Spacer(modifier = Modifier.height(16.dp))
            FilledTextField(
                value = state.name ?: "",
                onValueChange = onUpdateActivityName,
                label = R.string.moodTracking_updateActivity_enterActivityName_textField,
                multiline = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                isError = state.validatedName is IncorrectActivityName
            )
            AnimatedVisibility(visible = validatedActivityName is IncorrectActivityName) {
                if (validatedActivityName is IncorrectActivityName) {
                    ErrorText(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
                        text = when (val reason = validatedActivityName.reason) {
                            is IncorrectActivityName.Reason.Empty -> {
                                stringResource(R.string.moodTracking_updateActivity_nameEmpty_error)
                            }

                            is IncorrectActivityName.Reason.MoreThanMaxChars -> {
                                stringResource(
                                    id = R.string.moodTracking_updateActivity_nameMoreThanMaxChars_error,
                                    formatArgs = arrayOf(reason.maxChars)
                                )
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Title(text = stringResource(id = R.string.moodTracking_updateActivity_selectIcon_text))
            Spacer(modifier = Modifier.height(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 60.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(state.availableIconsList) {
                    IconItem(
                        icon = it,
                        contentDescriptionResId = R.string.moodTracking_updateActivity_activityIconContentDescription,
                        selectedIcon = state.selectedIcon,
                        onSelectIcon = onSelectIcon
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        ButtonWithIcon(
            iconResId = R.drawable.ic_save,
            labelResId = R.string.moodTracking_updateActivity_saveTrack_buttonText,
            onClick = onUpdate,
            isEnabled = state.allowUpdate
        )
    }
}