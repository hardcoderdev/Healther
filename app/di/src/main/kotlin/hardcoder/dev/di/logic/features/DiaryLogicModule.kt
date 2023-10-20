package hardcoder.dev.di.logic.features

import hardcoder.dev.logic.features.diary.diaryTag.DiaryTagNameValidator
import hardcoder.dev.logic.features.diary.diaryTrack.DiaryTrackContentValidator
import hardcoder.dev.logics.features.diary.DiaryDailyRateProvider
import hardcoder.dev.logics.features.diary.diaryAttachment.DiaryAttachmentManager
import hardcoder.dev.logics.features.diary.diaryAttachment.DiaryAttachmentProvider
import hardcoder.dev.logics.features.diary.diaryTag.DiaryTagCreator
import hardcoder.dev.logics.features.diary.diaryTag.DiaryTagDeleter
import hardcoder.dev.logics.features.diary.diaryTag.DiaryTagProvider
import hardcoder.dev.logics.features.diary.diaryTag.DiaryTagUpdater
import hardcoder.dev.logics.features.diary.diaryTrack.DiaryTrackCreator
import hardcoder.dev.logics.features.diary.diaryTrack.DiaryTrackDeleter
import hardcoder.dev.logics.features.diary.diaryTrack.DiaryTrackProvider
import hardcoder.dev.logics.features.diary.diaryTrack.DiaryTrackUpdater
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
            diaryTrackDao = get(),
            diaryAttachmentManager = get(),
            dispatchers = get(),
        )
    }

    single {
        DiaryTrackUpdater(
            diaryTrackDao = get(),
            diaryAttachmentManager = get(),
            dispatchers = get(),
        )
    }

    single {
        DiaryTrackDeleter(
            diaryTrackDao = get(),
            diaryAttachmentDao = get(),
            dispatchers = get(),
        )
    }

    single {
        DiaryTrackProvider(
            diaryTrackDao = get(),
            diaryAttachmentProvider = get(),
            dispatchers = get(),
        )
    }

    single {
        DiaryAttachmentManager(
            diaryAttachmentDao = get(),
            attachmentTypeIdMapper = get(),
            dispatchers = get(),
        )
    }

    single {
        DiaryAttachmentProvider(
            diaryAttachmentDao = get(),
            attachmentTypeIdMapper = get(),
            moodTrackProvider = get(),
            diaryTagProvider = get(),
            dispatchers = get(),
        )
    }

    single {
        DiaryTagCreator(
            diaryTagDao = get(),
            dispatchers = get(),
        )
    }

    single {
        DiaryTagUpdater(
            diaryTagDao = get(),
            dispatchers = get(),
        )
    }

    single {
        DiaryTagDeleter(
            diaryTagDao = get(),
            dispatchers = get(),
        )
    }

    single {
        DiaryTagProvider(
            diaryTagDao = get(),
            iconResourceProvider = get(),
            dispatchers = get(),
        )
    }
}