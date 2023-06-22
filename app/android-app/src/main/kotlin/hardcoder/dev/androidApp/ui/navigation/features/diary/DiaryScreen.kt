package hardcoder.dev.androidApp.ui.navigation.features.diary

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.diary.Diary

class DiaryScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Diary(
            onGoBack = navigator::pop,
            onCreateTrack = {
                navigator += DiaryCreationScreen()
            },
            onUpdateTrack = { diaryTrackId ->
                navigator += DiaryUpdateScreen(diaryTrackId)
            }
        )
    }
}