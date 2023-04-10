package hardcoder.dev.androidApp.ui.dashboard.diary

import android.content.Context
import androidx.annotation.StringRes
import hardcoder.dev.logic.R
import hardcoder.dev.logic.dashboard.features.diary.featureTag.FeatureTagPredefined
import hardcoder.dev.logic.dashboard.features.diary.featureTag.PredefinedFeatureTagProvider

class PredefinedFeatureTagProviderImpl(private val context: Context) : PredefinedFeatureTagProvider {

    override fun providePredefined() = listOf(
        create(nameResId = R.string.predefined_diary_filterFeatureType_noFeature),
        create(nameResId = R.string.predefined_diary_filterFeatureType_waterTrackingFilter),
        create(nameResId = R.string.predefined_diary_filterFeatureType_pedometerFilter),
        create(nameResId = R.string.predefined_diary_filterFeatureType_fastingFilter),
        create(nameResId = R.string.predefined_diary_filterFeatureType_moodTrackingFilter)
    )

    private fun create(@StringRes nameResId: Int) =
        FeatureTagPredefined(name = context.getString(nameResId))
}