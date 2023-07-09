package hardcoder.dev.androidApp.ui.screens.dashboard.featureItems

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hardcoder.dev.uikit.components.icon.Image
import hardcoder.dev.uikit.components.text.Title

@Composable
fun FeatureItemContent(
    @StringRes nameResId: Int,
    @DrawableRes imageResId: Int,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
    ) {
        Image(
            modifier = Modifier.size(60.dp),
            imageResId = imageResId,
            contentDescription = nameResId,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(Modifier.fillMaxWidth()) {
            Title(text = stringResource(id = nameResId))
            content()
        }
    }
}