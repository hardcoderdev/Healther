package hardcoder.dev.logic.features.diary.diaryTag

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.DiaryTag
import hardcoder.dev.logic.icons.IconResourceProvider
import hardcoder.dev.sqldelight.asFlowOfList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTag as DiaryTagEntity
import kotlinx.coroutines.flow.map

class DiaryTagProvider(
    private val appDatabase: AppDatabase,
    private val iconResourceProvider: IconResourceProvider,
    private val ioDispatcher: CoroutineDispatcher
) {

    fun provideAllDiaryTags() = appDatabase.diaryTagQueries
        .provideAllDiaryTags()
        .asFlowOfList(ioDispatcher) { diaryTagDatabase ->
            diaryTagDatabase.toEntity()
        }

    fun provideDiaryTagById(id: Int) = appDatabase.diaryTagQueries
        .provideDiaryTagById(id)
        .asFlow()
        .map { diaryTagDatabase -> diaryTagDatabase.executeAsOneOrNull()?.toEntity() }
        .flowOn(ioDispatcher)

    private fun DiaryTag.toEntity() = DiaryTagEntity(
        id = id,
        name = name,
        icon = iconResourceProvider.getIcon(iconId)
    )
}