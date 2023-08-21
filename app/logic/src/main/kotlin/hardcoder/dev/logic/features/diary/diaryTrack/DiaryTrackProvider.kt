package hardcoder.dev.logic.features.diary.diaryTrack

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.DiaryTrack
import hardcoder.dev.logic.reward.currency.CurrencyProvider
import hardcoder.dev.logic.features.FeatureType
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentGroup
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrack as DiaryTrackEntity

@OptIn(ExperimentalCoroutinesApi::class)
class DiaryTrackProvider(
    private val appDatabase: AppDatabase,
    private val diaryAttachmentProvider: DiaryAttachmentProvider,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val currencyProvider: CurrencyProvider,
) {

    fun provideAllDiaryTracksByDateRange(
        dateRange: ClosedRange<Instant>,
    ) = appDatabase.diaryTrackQueries
        .provideAllDiaryTracksByDateRange(dateRange.start, dateRange.endInclusive)
        .asFlow()
        .map { it.executeAsList() }
        .flatMapLatest { diaryTracks ->
            if (diaryTracks.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(
                    diaryTracks.map { diaryTrack ->
                        combine(
                            diaryAttachmentProvider.provideAttachmentOfDiaryTrackById(diaryTrack.id),
                            currencyProvider.isTrackCollected(
                                featureType = FeatureType.DIARY,
                                linkedTrackId = diaryTrack.id,
                            ),
                        ) { group, isRewardCollected ->
                            diaryTrack.toDiaryTrackEntity(
                                diaryAttachmentGroup = group,
                                isRewardCollected = isRewardCollected,
                            )
                        }
                    },
                ) {
                    it.toList()
                }
            }
        }.flowOn(dispatchers.io)

    fun provideDiaryTrackById(id: Int) = appDatabase.diaryTrackQueries
        .provideDiaryTrackById(id)
        .asFlow()
        .map { it.executeAsOneOrNull() }
        .flatMapLatest { diaryTrackDatabase ->
            diaryTrackDatabase?.let {
                combine(
                    diaryAttachmentProvider.provideAttachmentOfDiaryTrackById(diaryTrackDatabase.id),
                    currencyProvider.isTrackCollected(
                        featureType = FeatureType.DIARY,
                        linkedTrackId = diaryTrackDatabase.id,
                    ),
                ) { group, isRewardCollected ->
                    diaryTrackDatabase.toDiaryTrackEntity(
                        diaryAttachmentGroup = group,
                        isRewardCollected = isRewardCollected,
                    )
                }
            } ?: flowOf(null)
        }.flowOn(dispatchers.io)

    private fun DiaryTrack.toDiaryTrackEntity(
        diaryAttachmentGroup: DiaryAttachmentGroup?,
        isRewardCollected: Boolean,
    ) = DiaryTrackEntity(
        id = id,
        content = content,
        date = date,
        diaryAttachmentGroup = diaryAttachmentGroup,
        isRewardCollected = isRewardCollected,
    )
}