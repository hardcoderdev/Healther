package hardcoder.dev.logics.features.diary.diaryTag

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.DiaryTag
import hardcoder.dev.database.dao.features.diary.DiaryTagDao
import hardcoder.dev.database.entities.features.diary.DiaryTag
import hardcoder.dev.icons.IconResourceProvider
import hardcoder.dev.sqldelight.asFlowOfList
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.features.diary.DiaryTag as DiaryTagEntity

class DiaryTagProvider(
    private val diaryTagDao: DiaryTagDao,
    private val iconResourceProvider: IconResourceProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideAllDiaryTags() = diaryTagDao.provideAllDiaryTags()
        .asFlowOfList(dispatchers.io) { diaryTagDatabase ->
            diaryTagDatabase.toEntity()
        }

    fun provideDiaryTagById(id: Int) = diaryTagDao.provideDiaryTagById(id)
        .map { diaryTagDatabase -> diaryTagDatabase.executeAsOneOrNull()?.toEntity() }
        .flowOn(dispatchers.io)

    private fun DiaryTag.toEntity() = DiaryTagEntity(
        id = id,
        name = name,
        icon = iconResourceProvider.getIcon(iconId),
    )
}