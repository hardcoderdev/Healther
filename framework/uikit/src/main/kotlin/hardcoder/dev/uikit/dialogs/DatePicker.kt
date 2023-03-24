package hardcoder.dev.uikit.dialogs

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import kotlinx.datetime.LocalDate
import java.util.Calendar

@Composable
fun DatePicker(
    onUpdateSelectedDate: (LocalDate) -> Unit,
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
            onUpdateSelectedDate(LocalDate(selectedYear, selectedMonth, selectedDayOfMonth))
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