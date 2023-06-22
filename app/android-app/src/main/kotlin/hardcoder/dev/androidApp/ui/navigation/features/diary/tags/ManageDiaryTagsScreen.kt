package hardcoder.dev.androidApp.ui.navigation.features.diary.tags

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hardcoder.dev.androidApp.ui.features.diary.tags.ManageDiaryTags

class ManageDiaryTagsScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        ManageDiaryTags(
            onGoBack = navigator::pop,
            onCreateTag = {
                navigator += DiaryTagCreationScreen()
            },
            onUpdateTag = { diaryTagId ->
                navigator += DiaryTagUpdateScreen(diaryTagId)
            }
        )
    }
}