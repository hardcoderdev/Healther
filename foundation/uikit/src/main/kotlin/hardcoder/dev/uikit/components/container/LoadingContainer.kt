package hardcoder.dev.uikit.components.container

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import hardcoder.dev.controller.LoadingController

@Suppress("UNCHECKED_CAST")
@Composable
fun <T> LoadingContainer(
    controller: LoadingController<T>,
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() },
    loadedContent: @Composable (T) -> Unit,
) {
    LoadingContainer(
        controllers = listOf(controller),
        loadedContent = { loadedContent(it[0] as T) },
        loadingContent = loadingContent,
    )
}

@Suppress("UNCHECKED_CAST")
@Composable
fun <T1, T2> LoadingContainer(
    controller1: LoadingController<T1>,
    controller2: LoadingController<T2>,
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() },
    loadedContent: @Composable (T1, T2) -> Unit,
) {
    LoadingContainer(
        controllers = listOf(controller1, controller2),
        loadedContent = {
            loadedContent(
                it[0] as T1,
                it[1] as T2,
            )
        },
        loadingContent = loadingContent,
    )
}

@Suppress("UNCHECKED_CAST")
@Composable
fun <T1, T2, T3> LoadingContainer(
    controller1: LoadingController<T1>,
    controller2: LoadingController<T2>,
    controller3: LoadingController<T3>,
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() },
    loadedContent: @Composable (T1, T2, T3) -> Unit,
) {
    LoadingContainer(
        controllers = listOf(controller1, controller2, controller3),
        loadedContent = {
            loadedContent(
                it[0] as T1,
                it[1] as T2,
                it[2] as T3,
            )
        },
        loadingContent = loadingContent,
    )
}

@Suppress("UNCHECKED_CAST")
@Composable
fun <T1, T2, T3, T4> LoadingContainer(
    controller1: LoadingController<T1>,
    controller2: LoadingController<T2>,
    controller3: LoadingController<T3>,
    controller4: LoadingController<T4>,
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() },
    loadedContent: @Composable (T1, T2, T3, T4) -> Unit,
) {
    LoadingContainer(
        controllers = listOf(controller1, controller2, controller3, controller4),
        loadedContent = {
            loadedContent(
                it[0] as T1,
                it[1] as T2,
                it[2] as T3,
                it[3] as T4,
            )
        },
        loadingContent = loadingContent,
    )
}

@Suppress("UNCHECKED_CAST")
@Composable
fun <T1, T2, T3, T4, T5> LoadingContainer(
    controller1: LoadingController<T1>,
    controller2: LoadingController<T2>,
    controller3: LoadingController<T3>,
    controller4: LoadingController<T4>,
    controller5: LoadingController<T5>,
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() },
    loadedContent: @Composable (T1, T2, T3, T4, T5) -> Unit,
) {
    LoadingContainer(
        controllers = listOf(controller1, controller2, controller3, controller4, controller5),
        loadedContent = {
            loadedContent(
                it[0] as T1,
                it[1] as T2,
                it[2] as T3,
                it[3] as T4,
                it[4] as T5,
            )
        },
        loadingContent = loadingContent,
    )
}

@Composable
fun LoadingContainer(
    controllers: List<LoadingController<*>>,
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() },
    loadedContent: @Composable (List<*>) -> Unit,
) {
    val states = controllers.map { it.state.collectAsState().value }
    val isLoaded = states.all { it is LoadingController.State.Loaded }
    if (isLoaded) {
        loadedContent(
            states.map {
                (it as LoadingController.State.Loaded).data
            },
        )
    } else {
        loadingContent()
    }
}

@Composable
private fun DefaultLoadingContent() {
    // TODO do some loading moment if you need
}