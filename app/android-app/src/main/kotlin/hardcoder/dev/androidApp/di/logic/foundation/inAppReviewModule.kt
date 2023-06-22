package hardcoder.dev.androidApp.di.logic.foundation

import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import hardcoder.dev.in_app_review.AndroidReviewManager
import hardcoder.dev.in_app_review.ReviewManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val inAppReviewModule = module {
    singleOf(::FakeReviewManager)

    single {
        ReviewManagerFactory.create(androidContext())
    }

    single<ReviewManager> {
        AndroidReviewManager(
            reviewManager = get()
        )
    }

    single {
        AndroidReviewManager(
            reviewManager = get()
        )
    }
}