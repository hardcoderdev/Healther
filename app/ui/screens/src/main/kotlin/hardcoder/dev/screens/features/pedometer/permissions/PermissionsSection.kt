package hardcoder.dev.screens.features.pedometer.permissions

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
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButton
import hardcoder.dev.uikit.components.button.textIconButton.TextIconButtonConfig
import hardcoder.dev.uikit.components.card.Card
import hardcoder.dev.uikit.components.card.CardConfig
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Title
import hardcoderdev.healther.foundation.uikit.R

@Composable
fun ColumnScope.PermissionsSection(
    modifier: Modifier = Modifier,
    animationRepeatTimes: Int = LottieConstants.IterateForever,
    animationHeight: Dp = 400.dp,
    initial: Initial,
    rejected: Rejected?,
    onGrant: () -> Unit,
) {
    Column(Modifier.weight(2f)) {
        AnimatedVisibility(visible = rejected == null) {
            Title(text = stringResource(id = initial.titleResId))
        }
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(
                rejected?.animationResId ?: run {
                    initial.animationResId
                },
            ),
        )
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = animationRepeatTimes,
        )
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(visible = rejected != null) {
            rejected?.let {
                Card(
                    cardConfig = CardConfig.Static(
                        cardContent = {
                            Title(
                                modifier = Modifier.padding(16.dp),
                                text = stringResource(id = it.titleResId),
                            )
                            Description(
                                modifier = Modifier.padding(16.dp),
                                text = stringResource(id = it.descriptionResId),
                            )
                        },
                    ),
                )
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
    TextIconButton(
        textIconButtonConfig = TextIconButtonConfig.Filled(
            iconResId = R.drawable.ic_grant_permission,
            labelResId = R.string.default_grantPermission_buttonText,
            onClick = onGrant,
        ),
    )
}