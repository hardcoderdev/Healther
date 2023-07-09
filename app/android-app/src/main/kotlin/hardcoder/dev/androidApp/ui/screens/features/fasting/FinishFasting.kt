package hardcoder.dev.androidApp.ui.screens.features.fasting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import hardcoder.dev.androidApp.ui.formatters.MillisDistanceFormatter
import hardcoder.dev.controller.input.InputController
import hardcoder.dev.presentation.features.fasting.FastingViewModel
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButton
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButtonConfig
import hardcoder.dev.uikit.components.icon.Image
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Headline
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.text.textField.TextField
import hardcoderdev.healther.app.android.app.R
import org.koin.compose.koinInject

@Composable
fun FinishFasting(
    state: FastingViewModel.FastingState.Finished,
    noteInputController: InputController<String>,
    onClose: () -> Unit,
) {
    val lifecycleLocal = LocalLifecycleOwner.current.lifecycle
    val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_STOP || event == Lifecycle.Event.ON_DESTROY) {
            onClose()
        }
    }

    DisposableEffect(key1 = Unit) {
        lifecycleLocal.addObserver(observer)
        onDispose {
            lifecycleLocal.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(
            Modifier
                .weight(2f)
                .verticalScroll(rememberScrollState()),
        ) {
            CongratulationsSection(state = state)
            Spacer(modifier = Modifier.height(32.dp))
            AddNoteSection(noteInputController = noteInputController)
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextIconButton(
            textIconButtonConfig = TextIconButtonConfig.Filled(
                iconResId = R.drawable.ic_navigate_next,
                labelResId = R.string.fasting_finish_goNext_buttonText,
                onClick = onClose,
            ),
        )
    }
}

@Composable
private fun CongratulationsSection(state: FastingViewModel.FastingState.Finished) {
    val millisDistanceFormatter = koinInject<MillisDistanceFormatter>()

    val formattedFastingTime = millisDistanceFormatter.formatMillisDistance(
        distanceInMillis = state.timeLeftInMillis.inWholeMilliseconds,
        usePlurals = true,
        accuracy = if (state.isInterrupted) {
            MillisDistanceFormatter.Accuracy.MINUTES
        } else {
            MillisDistanceFormatter.Accuracy.HOURS
        },
    )

    val congratulationsStringResource = if (state.isInterrupted) {
        stringResource(
            id = R.string.fasting_finish_fail_results_text,
            formatArgs = arrayOf(formattedFastingTime),
        )
    } else {
        stringResource(id = R.string.fasting_finish_success_results_text)
    }

    Image(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        imageResId = R.drawable.fasting_finish_image,
        contentDescription = R.string.fasting_finish_image_contentDescription,
    )
    Spacer(modifier = Modifier.height(32.dp))
    Headline(text = stringResource(id = R.string.fasting_finish_text))
    Spacer(modifier = Modifier.height(16.dp))
    Description(text = congratulationsStringResource)
}

@Composable
private fun AddNoteSection(noteInputController: InputController<String>) {
    Title(text = stringResource(id = R.string.fasting_finish_addNoteOptional_text))
    Spacer(modifier = Modifier.height(16.dp))
    TextField(
        controller = noteInputController,
        label = R.string.fasting_creation_enterNote_textField,
        multiline = true,
        maxLines = 5,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
    )
}