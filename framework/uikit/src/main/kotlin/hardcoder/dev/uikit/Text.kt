package hardcoder.dev.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    style: TextStyle = LocalTextStyle.current,
    fontWeight: FontWeight? = null,
    iconModifier: Modifier = Modifier.padding(end = 8.dp),
    imageVector: ImageVector? = null,
    textAlign: TextAlign? = null,
    iconTint: Color = LocalContentColor.current,
    overflow: TextOverflow = TextOverflow.Clip,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement
    ) {
        if (imageVector != null) {
            Icon(
                modifier = iconModifier,
                contentDescription = null,
                imageVector = imageVector,
                tint = iconTint
            )
        }

        androidx.compose.material3.Text(
            text = text,
            style = style,
            color = color,
            fontWeight = fontWeight,
            fontSize = fontSize,
            textAlign = textAlign,
            overflow = overflow
        )
    }
}