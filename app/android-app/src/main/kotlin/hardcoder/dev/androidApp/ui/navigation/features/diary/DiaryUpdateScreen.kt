package hardcoder.dev.androidApp.ui.navigation.features.diary

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.diary.update.DiaryUpdate
import hardcoder.dev.androidApp.ui.navigation.features.diary.tags.ManageDiaryTagsScreen

data class DiaryUpdateScreen(val diaryTrackId: Int) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        DiaryUpdate(
            diaryTrackId = diaryTrackId,
            onGoBack = navigator::pop,
            onManageTags = {
                navigator += ManageDiaryTagsScreen()
            }
        )
    }
}