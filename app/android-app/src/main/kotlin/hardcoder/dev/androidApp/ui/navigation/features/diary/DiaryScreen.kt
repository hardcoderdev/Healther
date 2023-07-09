package hardcoder.dev.androidApp.ui.navigation.features.diary

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.screens.features.diary.Diary
import hardcoder.dev.presentation.features.diary.DiaryViewModel
import org.koin.androidx.compose.koinViewModel

class DiaryScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<DiaryViewModel>()

        Diary(
            viewModel = viewModel,
            onGoBack = navigator::pop,
            onCreateDiaryTrack = {
                navigator += DiaryCreationScreen()
            },
            onUpdateDiaryTrack = { diaryTrackId ->
                navigator += DiaryUpdateScreen(diaryTrackId)
            },
        )
    }
}