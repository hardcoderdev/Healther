package hardcoder.dev.mock.dataProviders.features

import android.content.Context
import hardcoder.dev.entities.features.moodTracking.MoodActivity
import hardcoder.dev.entities.features.moodTracking.MoodTrack
import hardcoder.dev.entities.features.moodTracking.MoodTrackingChartData
import hardcoder.dev.entities.features.moodTracking.MoodTrackingChartEntry
import hardcoder.dev.entities.features.moodTracking.MoodTrackingStatistics
import hardcoder.dev.entities.features.moodTracking.MoodType
import hardcoder.dev.entities.features.moodTracking.MoodWithActivities
import hardcoder.dev.icons.IconImpl
import hardcoderdev.healther.app.ui.resources.R
import kotlinx.datetime.Clock

object MoodTrackingMockDataProvider {

    fun moodActivitiesList(context: Context) = listOf(
        MoodActivity(
            id = 0,
            name = context.getString(R.string.predefined_moodType_name_bad),
            icon = IconImpl(id = 0, resourceId = R.drawable.ic_analytics),
        ),
        MoodActivity(
            id = 1,
            name = context.getString(R.string.predefined_drinkType_name_soup),
            icon = IconImpl(id = 1, resourceId = R.drawable.ic_take_a_bath),
        ),
        MoodActivity(
            id = 2,
            name = context.getString(R.string.predefined_drinkType_name_juice),
            icon = IconImpl(id = 2, resourceId = R.drawable.ic_fire),
        ),
        MoodActivity(
            id = 3,
            name = context.getString(R.string.predefined_drinkType_name_soda),
            icon = IconImpl(id = 2, resourceId = R.drawable.ic_history),
        ),
    )

    fun moodTypesList(context: Context) = listOf(
        MoodType(
            id = 0,
            name = context.getString(R.string.predefined_moodType_name_bad),
            icon = IconImpl(id = 0, resourceId = R.drawable.ic_analytics),
            positivePercentage = 10,
        ),
        MoodType(
            id = 1,
            name = context.getString(R.string.predefined_moodType_name_not_well),
            icon = IconImpl(id = 1, resourceId = R.drawable.ic_celebration),
            positivePercentage = 30,
        ),
        MoodType(
            id = 2,
            name = context.getString(R.string.predefined_moodType_name_neutral),
            icon = IconImpl(id = 1, resourceId = R.drawable.ic_inventory),
            positivePercentage = 50,
        ),
        MoodType(
            id = 3,
            name = context.getString(R.string.predefined_moodType_name_happy),
            icon = IconImpl(id = 1, resourceId = R.drawable.ic_shop),
            positivePercentage = 80,
        ),
    )

    fun moodTracksList(context: Context): List<MoodTrack> {
        val moodTypesList = moodTypesList(context)

        return listOf(
            MoodTrack(
                id = 0,
                date = Clock.System.now(),
                moodType = moodTypesList[0],
            ),
            MoodTrack(
                id = 1,
                date = Clock.System.now(),
                moodType = moodTypesList[1],
            ),
            MoodTrack(
                id = 2,
                date = Clock.System.now(),
                moodType = moodTypesList[2],
            ),
            MoodTrack(
                id = 3,
                date = Clock.System.now(),
                moodType = moodTypesList[3],
            ),

            MoodTrack(
                id = 4,
                date = Clock.System.now(),
                moodType = moodTypesList[0],
            ),
            MoodTrack(
                id = 5,
                date = Clock.System.now(),
                moodType = moodTypesList[1],
            ),
            MoodTrack(
                id = 6,
                date = Clock.System.now(),
                moodType = moodTypesList[2],
            ),
            MoodTrack(
                id = 7,
                date = Clock.System.now(),
                moodType = moodTypesList[3],
            ),
        )
    }

    fun moodWithActivitiesList(context: Context): List<MoodWithActivities> {
        val moodTracksList = moodTracksList(context)
        val moodActivitiesList = moodActivitiesList(context)

        return listOf(
            MoodWithActivities(
                moodTrack = moodTracksList[0],
                moodActivityList = moodActivitiesList,
            ),
            MoodWithActivities(
                moodTrack = moodTracksList[1],
                moodActivityList = emptyList(),
            ),
            MoodWithActivities(
                moodTrack = moodTracksList[2],
                moodActivityList = moodActivitiesList,
            ),
            MoodWithActivities(
                moodTrack = moodTracksList[3],
                moodActivityList = emptyList(),
            ),
            MoodWithActivities(
                moodTrack = moodTracksList[4],
                moodActivityList = moodActivitiesList,
            ),
        )
    }

    fun moodTrackingStatistics(context: Context) = MoodTrackingStatistics(
        happyMoodCount = 4,
        neutralMoodCount = 3,
        notWellMoodCount = 3,
        badMoodCount = 2,
        averageMoodType = moodTypesList(context)[0],
    )

    fun moodTrackingChartData() = MoodTrackingChartData(
        entriesList = listOf(
            MoodTrackingChartEntry(
                from = 0,
                to = 1,
            ),
            MoodTrackingChartEntry(
                from = 2,
                to = 3,
            ),
            MoodTrackingChartEntry(
                from = 4,
                to = 5,
            ),
            MoodTrackingChartEntry(
                from = 6,
                to = 7,
            ),
        ),
    )
}