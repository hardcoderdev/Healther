package hardcoder.dev.screens.features.moodTracking.moodType

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hardcoder.dev.entities.features.moodTracking.MoodType
import hardcoder.dev.icons.IconImpl
import hardcoder.dev.icons.resourceId
import hardcoder.dev.uikit.components.text.Label
import hardcoder.dev.uikit.values.HealtherTheme
import hardcoderdev.healther.app.ui.resources.R

@Composable
fun MoodItem(
    modifier: Modifier = Modifier,
    moodType: MoodType,
) {
    Column(
        modifier = modifier
            .width(130.dp)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.height(60.dp),
            painter = painterResource(id = moodType.icon.resourceId),
            contentDescription = moodType.name,
            alignment = Alignment.Center,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Label(text = moodType.name)
    }
}

@Preview
@Composable
private fun MoodItemPreview() {
    HealtherTheme {
        MoodItem(
            moodType =
            MoodType(
                id = 0,
                name = stringResource(R.string.predefined_moodType_name_bad),
                icon = IconImpl(id = 0, resourceId = R.drawable.ic_analytics),
                positivePercentage = 70,
            )
        )
    }
}