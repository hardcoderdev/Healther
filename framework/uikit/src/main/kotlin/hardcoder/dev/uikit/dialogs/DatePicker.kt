package hardcoder.dev.uikit.dialogs

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import kotlinx.datetime.LocalDateTime
import java.util.Calendar

@Composable
fun DatePicker(
    onUpdateSelectedDate: (LocalDateTime) -> Unit,
    isShowing: Boolean,
    onClose: () -> Unit
) {
    val calendar = Calendar.getInstance()

    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            onUpdateSelectedDate(
                LocalDateTime(
                    selectedYear,
                    selectedMonth + 1,
                    selectedDayOfMonth,
                    12,
                    0,
                    0
                )
            )
            onClose()
        },
        year,
        month,
        dayOfMonth
    ).apply {
        datePicker.maxDate = Calendar.getInstance().timeInMillis
    }

    if (isShowing) datePickerDialog.show()
}