package hardcoder.dev.androidApp.ui.features.moodTracking.moodType.providers

import android.content.Context
import androidx.annotation.StringRes
import hardcoder.dev.logic.features.moodTracking.moodType.MoodTypePredefined
import hardcoder.dev.logic.features.moodTracking.moodType.PredefinedMoodTypeProvider
import hardcoderdev.healther.app.logic.R

class PredefinedMoodTypeProviderImpl(
    private val context: Context,
    private val moodTypeIconProvider: MoodTypeIconProvider
) : PredefinedMoodTypeProvider {

    override fun providePredefined() = listOf(
        create(
            nameResId = R.string.predefined_moodType_name_happy,
            iconId = 0,
            positivePercentage = 100
        ),
        create(
            nameResId = R.string.predefined_moodType_name_neutral,
            iconId = 1,
            positivePercentage = 80
        ),
        create(
            nameResId = R.string.predefined_moodType_name_not_well,
            iconId = 2,
            positivePercentage = 60
        ),
        create(
            nameResId = R.string.predefined_moodType_name_bad,
            iconId = 3,
            positivePercentage = 40
        )
    )

    private fun create(
        @StringRes nameResId: Int,
        iconId: Int,
        positivePercentage: Int
    ) = MoodTypePredefined(
        name = context.getString(nameResId),
        icon = moodTypeIconProvider.getIcon(iconId),
        positivePercentage = positivePercentage
    )
}