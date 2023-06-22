package hardcoder.dev.androidApp.ui.navigation.features.diary

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.diary.create.DiaryCreation
import hardcoder.dev.androidApp.ui.navigation.features.diary.tags.ManageDiaryTagsScreen

class DiaryCreationScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        DiaryCreation(
            onGoBack = navigator::pop,
            onManageTags = {
                navigator += ManageDiaryTagsScreen()
            }
        )
    }
}