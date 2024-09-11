package hardcoder.dev.screens.settings

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
import hardcoder.dev.blocks.components.button.textIconButton.TextIconButton
import hardcoder.dev.blocks.components.button.textIconButton.TextIconButtonConfig
import hardcoder.dev.blocks.components.containers.ScaffoldWrapper
import hardcoder.dev.blocks.components.text.Title
import hardcoder.dev.blocks.components.topBar.TopBarConfig
import hardcoder.dev.blocks.components.topBar.TopBarType
import hardcoder.dev.controller.LoadingController
import hardcoder.dev.entities.appPreferences.AppPreference
import hardcoder.dev.mock.controllers.MockControllersProvider
import hardcoder.dev.mock.dataProviders.AppPreferencesMockDataProvider
import hardcoder.dev.uikit.components.container.LoadingContainer
import hardcoder.dev.uikit.preview.screens.HealtherScreenPhonePreviews
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R

private const val DEVELOPER_PAGE_LINK =
    "https://play.google.com/store/apps/dev?id=7383576086355039907"

@Composable
fun Settings(
    preferencesLoadingController: LoadingController<AppPreference>,
    onGoBack: () -> Unit,
) {
    ScaffoldWrapper(
        content = {
            SettingsContent(
                preferencesLoadingController = preferencesLoadingController,
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
            }
        },
    )
}

@HealtherScreenPhonePreviews
@Composable
private fun SettingsPreview() {
    HealtherTheme {
        Settings(
            onGoBack = {},
            preferencesLoadingController = MockControllersProvider.loadingController(
                data = AppPreferencesMockDataProvider.appPreferences(),
            ),
        )
    }
}