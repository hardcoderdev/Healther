package hardcoder.dev.androidApp.di.logic

import hardcoder.dev.androidApp.di.logic.foundation.coroutinesModule
import hardcoder.dev.androidApp.di.logic.foundation.identificationModule
import hardcoder.dev.androidApp.di.logic.foundation.inAppReviewModule
import org.koin.dsl.module

val foundationLogicModule = module {
   includes(
       coroutinesModule,
       identificationModule,
       inAppReviewModule,
   )
}