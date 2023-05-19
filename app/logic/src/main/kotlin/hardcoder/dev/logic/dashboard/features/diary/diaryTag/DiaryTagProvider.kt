package hardcoder.dev.logic.dashboard.features.diary.diaryTag

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.DiaryTag
import hardcoder.dev.logic.icons.IconResourceProvider
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTag as DiaryTagEntity
import kotlinx.coroutines.flow.map

class DiaryTagProvider(
    private val appDatabase: AppDatabase,
    private val iconResourceProvider: IconResourceProvider
) {

    fun provideAllDiaryTags() = appDatabase.diaryTagQueries
        .provideAllDiaryTags()
        .asFlow()
        .map {
            it.executeAsList().map {  diaryTagDatabase ->
                diaryTagDatabase.toEntity()
            }
        }

    fun provideDiaryTagById(id: Int) = appDatabase.diaryTagQueries
        .provideDiaryTagById(id)
        .asFlow()
        .map { diaryTagQuery ->
            diaryTagQuery.executeAsOneOrNull()?.toEntity()
        }

    private fun DiaryTag.toEntity() = DiaryTagEntity(
        id = id,
        name = name,
        icon = iconResourceProvider.getIcon(iconId)
    )
}