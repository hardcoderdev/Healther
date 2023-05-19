package hardcoder.dev.sqldelight

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import hardcoder.dev.coroutines.mapItems
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

fun <T : Any, R : Any> Query<T>.asFlowOfList(
    coroutineDispatcher: CoroutineDispatcher,
    transform: suspend (T) -> R
) = asFlow()
    .mapToList(coroutineDispatcher)
    .mapItems(transform)
    .flowOn(Dispatchers.Default)