package hardcoder.dev.healther.ui.base.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T, R> Flow<List<T>>.mapItems(transform: (T) -> R) = map { list ->
    list.map { transform(it) }
}