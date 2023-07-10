package hardcoder.dev.uikit.components.icon

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import hardcoder.dev.uikit.preview.UiKitPreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.foundation.uikit.R
import androidx.compose.foundation.Image as ComposeFoundationImage

@Composable
fun Image(
    modifier: Modifier = Modifier,
    imageResId: Int,
    contentScale: ContentScale = ContentScale.Fit,
    @StringRes contentDescription: Int? = null,
) {
    ComposeFoundationImage(
        modifier = modifier,
        contentScale = contentScale,
        painter = painterResource(id = imageResId),
        contentDescription = contentDescription?.let { stringResource(id = it) },
    )
}

@UiKitPreview
@Composable
private fun ImagePreview() {
    HealtherThemePreview {
        Image(
            imageResId = R.drawable.uikit_sample_image,
        )
    }
}