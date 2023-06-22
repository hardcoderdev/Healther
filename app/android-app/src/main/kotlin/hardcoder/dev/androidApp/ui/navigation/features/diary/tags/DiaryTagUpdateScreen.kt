package hardcoder.dev.androidApp.ui.navigation.features.diary.tags

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.diary.tags.update.DiaryTagUpdate

data class DiaryTagUpdateScreen(val diaryTag: Int) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        DiaryTagUpdate(
            tagId = diaryTag,
            onGoBack = navigator::pop
        )
    }
}
