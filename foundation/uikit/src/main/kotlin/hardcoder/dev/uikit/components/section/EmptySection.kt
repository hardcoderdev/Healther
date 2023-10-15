package hardcoder.dev.uikit.components.section

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import hardcoder.dev.uikit.components.text.Description
import hardcoder.dev.uikit.components.text.Title
import hardcoder.dev.uikit.preview.elements.HealtherUiKitPhonePreview
import hardcoder.dev.uikit.values.HealtherThemePreview
import hardcoderdev.healther.foundation.uikit.R

@Composable
fun EmptySection(
    modifier: Modifier = Modifier,
    @StringRes emptyTitleResId: Int = R.string.default_nowEmpty_text,
    @RawRes lottieAnimationResId: Int = R.raw.empty_shake_box,
    animationRepeatTimes: Int = LottieConstants.IterateForever,
    animationHeight: Dp = 400.dp,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Title(text = stringResource(id = emptyTitleResId))
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieAnimationResId))
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = animationRepeatTimes,
        )
        Spacer(modifier = Modifier.height(16.dp))
        LottieAnimation(
            modifier = modifier
                .fillMaxWidth()
                .height(animationHeight),
            composition = composition,
            progress = { progress },
        )
    }
}

@Composable
fun EmptyBlock(
    modifier: Modifier = Modifier,
    @StringRes emptyTitleResId: Int = R.string.default_nowEmpty_text,
    @RawRes lottieAnimationResId: Int = R.raw.empty_shake_box,
    animationRepeatTimes: Int = LottieConstants.IterateForever,
) {
    Description(text = stringResource(id = emptyTitleResId))
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieAnimationResId))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = animationRepeatTimes,
    )
    Spacer(modifier = Modifier.height(16.dp))
    LottieAnimation(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        composition = composition,
        progress = { progress },
    )
}

@HealtherUiKitPhonePreview
@Composable
private fun EmptySectionPreview() {
    HealtherThemePreview {
        Column(modifier = Modifier.fillMaxSize()) {
            EmptySection(
                animationRepeatTimes = LottieConstants.IterateForever,
                emptyTitleResId = R.string.default_nowEmpty_text,
                modifier = Modifier.height(300.dp),
                lottieAnimationResId = R.raw.empty_shake_box,
            )
        }
    }
}

@HealtherUiKitPhonePreview
@Composable
private fun EmptyBlockPreview() {
    HealtherThemePreview {
        Column(modifier = Modifier.fillMaxSize()) {
            EmptyBlock(
                animationRepeatTimes = LottieConstants.IterateForever,
                emptyTitleResId = R.string.default_nowEmpty_text,
                modifier = Modifier.height(300.dp),
                lottieAnimationResId = R.raw.empty_shake_box,
            )
        }
    }
}