package hardcoder.dev.uikit.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.basis.next
import epicarchitect.calendar.compose.basis.previous
import epicarchitect.calendar.compose.datepicker.state.EpicDatePickerState
import hardcoder.dev.uikit.icons.Icon
import hardcoder.dev.uikit.text.Title
import hardcoderdev.healther.foundation.uikit.R
import kotlinx.coroutines.launch
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CustomMonthHeader(
    state: EpicDatePickerState,
    month: EpicMonth
) {
    val coroutineScope = rememberCoroutineScope()

    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            iconResId = R.drawable.ic_top_bar_back,
            modifier = Modifier
                .padding(end = 32.dp)
                .clickable {
                    coroutineScope.launch {
                        state.pagerState.scrollToMonth(month.previous())
                    }
                }
        )
        Title(
            text = month.month
                .getDisplayName(TextStyle.FULL, Locale.getDefault())
                .lowercase()
                .replaceFirstChar { it.titlecase() }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Title(text = month.year.toString())
        Icon(
            iconResId = R.drawable.ic_top_bar_back,
            modifier = Modifier
                .padding(start = 32.dp)
                .rotate(-180f)
                .clickable {
                    coroutineScope.launch {
                        state.pagerState.scrollToMonth(month.next())
                    }
                }
        )
    }
}