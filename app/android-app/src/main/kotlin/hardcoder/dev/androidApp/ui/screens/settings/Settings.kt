package hardcoder.dev.androidApp.ui.screens.settings

import android.content.Intent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.controller.request.SingleRequestController
import hardcoder.dev.logic.appPreferences.AppPreference
import hardcoder.dev.presentation.settings.SettingsViewModel
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonConfig
import hardcoder.dev.uikit.components.button.requestButton.RequestButtonWithIcon
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButton
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButtonConfig
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.components.container.ScaffoldWrapper
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.components.topBar.TopBarConfig
import hardcoder.dev.uikit.components.topBar.TopBarType
import hardcoderdev.healther.app.android.app.R

private const val DEVELOPER_PAGE_LINK =
    "https://play.google.com/store/apps/dev?id=7383576086355039907"

@Composable
fun Settings(
    viewModel: SettingsViewModel,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            SettingsContent(
                preferencesLoadingController = viewModel.preferencesLoadingController,
                appReviewRequestController = viewModel.appReviewRequestController,
            )
        },
        topBarConfig = TopBarConfig(
            type = TopBarType.TopBarWithNavigationBack(
                titleResId = R.string.settings_title_topBar,
                onGoBack = onGoBack,
            ),
        ),
    )
}

@Composable
private fun SettingsContent(
    preferencesLoadingController: LoadingController<AppPreference>,
    appReviewRequestController: SingleRequestController,
) {
    val context = LocalContext.current

    LoadingContainer(
        controller = preferencesLoadingController,
        loadedContent = { preferences ->
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Title(text = stringResource(id = R.string.settings_other_text))
                Spacer(modifier = Modifier.height(16.dp))
                TextIconButton(
                    textIconButtonConfig = TextIconButtonConfig.Filled(
                        iconResId = R.drawable.ic_apps,
                        labelResId = R.string.settings_moreApps_text,
                        onClick = {
                            Intent().apply {
                                action = Intent.ACTION_VIEW
                                data = DEVELOPER_PAGE_LINK.toUri()
                            }.also {
                                context.startActivity(it)
                            }
                        },
                    ),
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextIconButton(
                    textIconButtonConfig = TextIconButtonConfig.Filled(
                        iconResId = R.drawable.ic_coffee,
                        labelResId = R.string.settings_supportCreator_text,
                        onClick = {
                            // TODO MAKE BILLING LOGIC
                        },
                    ),
                )
                if (preferences.lastAppReviewRequestTime == null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    RequestButtonWithIcon(
                        requestButtonConfig = RequestButtonConfig.Filled(
                            controller = appReviewRequestController,
                            iconResId = R.drawable.ic_rate,
                            labelResId = R.string.settings_rateApp_text,
                        ),
                    )
                }
            }
        },
    )
}