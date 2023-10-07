package hardcoder.dev.di.logic.features

import hardcoder.dev.logic.features.diary.DiaryDailyRateProvider
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentManager
import hardcoder.dev.logic.features.diary.diaryAttachment.DiaryAttachmentProvider
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagCreator
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagDeleter
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagNameValidator
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagProvider
import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagUpdater
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackContentValidator
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackCreator
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackDeleter
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackProvider
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackUpdater
import hardcoder.dev.mappers.features.diary.AttachmentTypeIdMapper
import hardcoder.dev.resources.features.diary.DiaryTagIconProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val diaryLogicModule = module {
    singleOf(::AttachmentTypeIdMapper)
    singleOf(::DiaryTagIconProvider)
    singleOf(::DiaryTagNameValidator)
    singleOf(::DiaryTrackContentValidator)
    singleOf(::DiaryDailyRateProvider)

    single {
        DiaryTrackCreator(
            idGenerator = get(),
            appDatabase = get(),
            diaryAttachmentManager = get(),
            dispatchers = get(),
        )
    }

    single {
        DiaryTrackUpdater(
            appDatabase = get(),
            diaryAttachmentManager = get(),
            dispatchers = get(),
        )
    }

    single {
        DiaryTrackDeleter(
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        DiaryTrackProvider(
            appDatabase = get(),
            diaryAttachmentProvider = get(),
            dispatchers = get(),
        )
    }

    single {
        DiaryAttachmentManager(
            idGenerator = get(),
            appDatabase = get(),
            attachmentTypeIdMapper = get(),
            dispatchers = get(),
        )
    }

    single {
        DiaryAttachmentProvider(
            appDatabase = get(),
            attachmentTypeIdMapper = get(),
            fastingTrackProvider = get(),
            moodTrackProvider = get(),
            diaryTagProvider = get(),
            dispatchers = get(),
        )
    }

    single {
        DiaryTagCreator(
            idGenerator = get(),
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        DiaryTagUpdater(
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        DiaryTagDeleter(
            appDatabase = get(),
            dispatchers = get(),
        )
    }

    single {
        DiaryTagProvider(
            appDatabase = get(),
            iconResourceProvider = get(),
            dispatchers = get(),
        )
    }
}