package hardcoder.dev.androidApp.ui.settings

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import hardcoder.dev.androidApp.di.LocalPresentationModule
import hardcoder.dev.healther.R
import hardcoder.dev.presentation.settings.SettingsViewModel
import hardcoder.dev.uikit.ScaffoldWrapper
import hardcoder.dev.uikit.TopBarConfig
import hardcoder.dev.uikit.TopBarType
import hardcoder.dev.uikit.buttons.ButtonWithIcon
import hardcoder.dev.uikit.text.Title

private const val DEVELOPER_PAGE_LINK = "https://play.google.com/store/apps/dev?id=7383576086355039907"

@Composable
fun SettingsScreen(onGoBack: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel { presentationModule.getSettingsViewModel() }
    val state = viewModel.state.collectAsState()

    ScaffoldWrapper(
        content = {
            SettingsContent(
                state = state.value,
                onLaunchReviewFlow = viewModel::launchInAppReviewFlow
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.settings_title_topBar,
                onGoBack = onGoBack
            )
        )
    )
}

@Composable
private fun SettingsContent(
    state: SettingsViewModel.State,
    onLaunchReviewFlow: () -> Unit
) {
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Title(text = stringResource(id = R.string.settings_other_text))
        Spacer(modifier = Modifier.height(16.dp))
        ButtonWithIcon(
            iconResId = R.drawable.ic_apps,
            labelResId = R.string.settings_moreApps_text,
            onClick = {
                Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = DEVELOPER_PAGE_LINK.toUri()
                }.also {
                    context.startActivity(it)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        ButtonWithIcon(
            iconResId = R.drawable.ic_coffee,
            labelResId = R.string.settings_supportCreator_text,
            onClick = {
                // TODO MAKE BILLING LOGIC
            }
        )
        if (!state.isAppAlreadyRated) {
            Spacer(modifier = Modifier.height(16.dp))
            ButtonWithIcon(
                iconResId = R.drawable.ic_rate,
                labelResId = R.string.settings_rateApp_text,
                onClick = onLaunchReviewFlow
            )
        }
    }
}