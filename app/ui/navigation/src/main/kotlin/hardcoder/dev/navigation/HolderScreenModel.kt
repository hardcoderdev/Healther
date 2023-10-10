package hardcoder.dev.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import hardcoder.dev.viewmodel.ViewModel
import org.koin.compose.koinInject

class HolderScreenModel<T : ViewModel>(val viewModel: T) : ScreenModel {

    override fun onDispose() {
        super.onDispose()
        viewModel.onCleared()
    }
}

@Composable
inline fun <reified T : ViewModel> Screen.injectViewModel(
    tag: String? = null,
): T {
    val viewModel = koinInject<T>()
    return rememberScreenModel(tag = tag) {
        HolderScreenModel(viewModel = viewModel)
    }.viewModel
}