package hardcoder.dev.androidApp.ui.features.moodTracking.hobby.create

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
import hardcoder.dev.logic.features.moodTracking.hobby.IncorrectHobbyName
import hardcoder.dev.logic.icons.LocalIcon
import hardcoder.dev.presentation.features.moodTracking.hobby.HobbyCreateViewModel
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.IconTextButton
import hardcoder.dev.uikit.text.ErrorText
import hardcoder.dev.uikit.text.FilledTextField
import hardcoder.dev.uikit.text.Title

@Composable
fun CreateHobbyTrackScreen(onGoBack: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.getHobbyTrackCreateViewModel()
    }
    val state = viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.value.creationState) {
        if (state.value.creationState is HobbyCreateViewModel.CreationState.Executed) {
            onGoBack()
        }
    }

    ScaffoldWrapper(
        content = {
            CreateHobbyTrackContent(
                state = state.value,
                onSelectIcon = viewModel::updateSelectedIcon,
                onUpdateHobbyName = viewModel::updateHobbyName,
                onCreateTrack = viewModel::createTrack
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.moodTracking_HobbyCreateTrack_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
fun CreateHobbyTrackContent(
    state: HobbyCreateViewModel.State,
    onUpdateHobbyName: (String) -> Unit,
    onSelectIcon: (LocalIcon) -> Unit,
    onCreateTrack: () -> Unit
) {
    val validatedHobbyName = state.validatedName

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(Modifier.weight(2f)) {
            Title(text = stringResource(id = R.string.moodTracking_HobbyCreateTrack_enter_name_text))
            Spacer(modifier = Modifier.height(16.dp))
            FilledTextField(
                value = state.name ?: "",
                onValueChange = onUpdateHobbyName,
                label = R.string.moodTracking_HobbyCreateTrack_enterHobbyName_textField,
                multiline = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                isError = state.validatedName is IncorrectHobbyName
            )
            AnimatedVisibility(visible = validatedHobbyName is IncorrectHobbyName) {
                if (validatedHobbyName is IncorrectHobbyName) {
                    ErrorText(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
                        text = when (validatedHobbyName.reason) {
                            is IncorrectHobbyName.Reason.Empty -> {
                                stringResource(R.string.moodTracking_HobbyCreateTrack_nameEmpty_error)
                            }

                            is IncorrectHobbyName.Reason.MoreThanMaxChars -> {
                                stringResource(R.string.moodTracking_HobbyCreateTrack_nameMoreThanMaxChars_error)
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Title(text = stringResource(id = R.string.moodTracking_HobbyCreateTrack_selectIcon_text))
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
                        selectedIcon = state.selectedIcon,
                        contentDescriptionResId = R.string.moodTracking_HobbyCreateTrack_iconContentDescription,
                        onSelectIcon = onSelectIcon
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        IconTextButton(
            iconResId = R.drawable.ic_save,
            labelResId = R.string.moodTracking_HobbyCreateTrack_saveTrack_buttonText,
            onClick = onCreateTrack,
            isEnabled = state.creationAllowed
        )
    }
}