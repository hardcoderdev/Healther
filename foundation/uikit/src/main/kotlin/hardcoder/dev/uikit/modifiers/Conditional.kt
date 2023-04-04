package hardcoder.dev.uikit.modifiers

import androidx.compose.ui.Modifier

fun Modifier.conditional(condition: Boolean, modifier : Modifier.() -> Modifier) : Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}