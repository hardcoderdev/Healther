package hardcoder.dev.uikit.sections

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import hardcoder.dev.uikit.buttons.ButtonWithIcon
import hardcoder.dev.uikit.card.StaticCard
import hardcoder.dev.uikit.text.Description
import hardcoder.dev.uikit.text.Title
import hardcoderdev.healther.foundation.uikit.R

data class Rejected(
    @RawRes val animationResId: Int,
    @StringRes val titleResId: Int,
    @StringRes val descriptionResId: Int,
)

data class Initial(
    @StringRes val titleResId: Int = R.string.default_nowEmpty_text,
    @RawRes val animationResId: Int = R.raw.empty_shake_box,
)

@Composable
fun ColumnScope.PermissionsSection(
    modifier: Modifier = Modifier,
    animationRepeatTimes: Int = LottieConstants.IterateForever,
    animationHeight: Dp = 400.dp,
    rejected: Rejected?,
    initial: Initial,
    onGrant: () -> Unit
) {
    Column(Modifier.weight(2f)) {
        AnimatedVisibility(visible = rejected == null) {
            Title(text = stringResource(id = initial.titleResId))
        }
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(
                rejected?.animationResId ?: run {
                    initial.animationResId
                }
            )
        )
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = animationRepeatTimes
        )
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(visible = rejected != null) {
            rejected?.let {
                StaticCard {
                    Title(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(id = it.titleResId),
                    )
                    Description(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(id = it.descriptionResId)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LottieAnimation(
            modifier = modifier
                .fillMaxWidth()
                .height(animationHeight),
            composition = composition,
            progress = { progress },
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
    ButtonWithIcon(
        iconResId = R.drawable.ic_grant_permission,
        labelResId = R.string.default_grantPermission_buttonText,
        onClick = onGrant
    )
}

