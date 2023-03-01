package hardcoder.dev.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.toast(@StringRes msgResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, getString(msgResId), duration).show()
}

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}