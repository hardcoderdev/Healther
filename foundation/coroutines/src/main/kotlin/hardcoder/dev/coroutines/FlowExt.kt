package hardcoder.dev.coroutines

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T, R> Flow<List<T>>.mapItems(transform: suspend (T) -> R) = map { list ->
    list.map { transform(it) }
}