package hardcoder.dev.androidApp.ui.icons

import android.content.Context
import hardcoder.dev.healther.R
import hardcoder.dev.logic.icons.IconResourceProvider

class IconProvider(private val context: Context) : IconResourceProvider {

    override fun provideWaterTrackingIconResources(): List<String> {
        return provideWaterTrackingIcons()
    }

    override fun provideMoodTrackingHobbyIconResources(): List<String> {
        return provideMoodTrackingHobbyIcons()
    }

    override fun provideMoodTrackingMoodTypesIconsResources(): List<String> {
        return provideMoodTrackingMoodTypeIcons()
    }

    // TODO DIVIDE OTHER ICONS AND ADD MORE ICONS

    private fun provideWaterTrackingIcons() = listOf(
        *provideAllSportiveIcons().toTypedArray()
    ).map {
        context.resources.getResourceEntryName(it)
    }

    private fun provideMoodTrackingHobbyIcons() = listOf(
        *provideAllSportiveIcons().toTypedArray()
    ).map {
        context.resources.getResourceEntryName(it)
    }

    private fun provideMoodTrackingMoodTypeIcons() = listOf(
        *provideAllSportiveIcons().toTypedArray()
    ).map {
        context.resources.getResourceEntryName(it)
    }

    fun provideAllIcons() = listOf(
        *provideAllSportiveIcons().toTypedArray(),
        *provideBadHabitIcons().toTypedArray(),
        *provideSocialIcons().toTypedArray(),
        *provideTravelIcons().toTypedArray(),
        *provideOtherIcons().toTypedArray()
    ).map {
        context.resources.getResourceEntryName(it)
    }

    private fun provideAllSportiveIcons() = listOf(
        R.drawable.ic_mma, R.drawable.ic_tennis, R.drawable.ic_soccer,
        R.drawable.ic_baseball, R.drawable.ic_volleyball, R.drawable.ic_basketball,
        R.drawable.ic_golf, R.drawable.ic_gymnastics, R.drawable.ic_martial_arts,
        R.drawable.ic_esports, R.drawable.ic_handball, R.drawable.ic_hockey,
        R.drawable.ic_roller_skating, R.drawable.ic_rugby, R.drawable.ic_motorsports,
        R.drawable.ic_snowboarding
    )

    private fun provideBadHabitIcons() = listOf(
        R.drawable.ic_wine, R.drawable.ic_beer, R.drawable.ic_smoking,
        R.drawable.ic_vaping, R.drawable.ic_videogame
    )

    private fun provideSocialIcons() = listOf(
        R.drawable.ic_celebration, R.drawable.ic_fire, R.drawable.ic_group,
        R.drawable.ic_text_chat, R.drawable.ic_video_chat, R.drawable.ic_voice_chat,
        R.drawable.ic_theater
    )

    private fun provideTravelIcons() = listOf(
        R.drawable.ic_airplane, R.drawable.ic_train, R.drawable.ic_tour
    )

    private fun provideOtherIcons() = listOf(
        R.drawable.ic_volunteer, R.drawable.ic_work, R.drawable.ic_tips,
        R.drawable.ic_take_a_bath, R.drawable.ic_gardening
    )
}