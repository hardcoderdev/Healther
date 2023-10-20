package hardcoder.dev.logics.features.diary.diaryTag

import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.coroutines.mapItems
import hardcoder.dev.database.dao.features.diary.DiaryTagDao
import hardcoder.dev.database.entities.features.diary.DiaryTag
import hardcoder.dev.icons.IconResourceProvider
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.entities.features.diary.DiaryTag as DiaryTagEntity

class DiaryTagProvider(
    private val diaryTagDao: DiaryTagDao,
    private val iconResourceProvider: IconResourceProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideAllDiaryTags() = diaryTagDao
        .provideAllDiaryTags()
        .mapItems { it.toEntity() }
        .flowOn(dispatchers.io)

    fun provideDiaryTagById(id: Int) = diaryTagDao
        .provideDiaryTagById(id)
        .map { it?.toEntity() }
        .flowOn(dispatchers.io)

    private fun DiaryTag.toEntity() = DiaryTagEntity(
        id = id,
        name = name,
        icon = iconResourceProvider.getIcon(iconId),
    )
}