package hardcoder.dev.logic.features.diary.diaryTag

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.DiaryTag
import hardcoder.dev.sqldelight.asFlowOfList
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTag as DiaryTagEntity

class DiaryTagProvider(
    private val appDatabase: AppDatabase,
    private val iconResourceProvider: hardcoder.dev.icons.IconResourceProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideAllDiaryTags() = appDatabase.diaryTagQueries
        .provideAllDiaryTags()
        .asFlowOfList(dispatchers.io) { diaryTagDatabase ->
            diaryTagDatabase.toEntity()
        }

    fun provideDiaryTagById(id: Int) = appDatabase.diaryTagQueries
        .provideDiaryTagById(id)
        .asFlow()
        .map { diaryTagDatabase -> diaryTagDatabase.executeAsOneOrNull()?.toEntity() }
        .flowOn(dispatchers.io)

    private fun DiaryTag.toEntity() = DiaryTagEntity(
        id = id,
        name = name,
        icon = iconResourceProvider.getIcon(iconId),
    )
}