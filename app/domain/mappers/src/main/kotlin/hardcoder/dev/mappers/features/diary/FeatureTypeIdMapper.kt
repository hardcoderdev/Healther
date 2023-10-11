package hardcoder.dev.mappers.features.diary

import hardcoder.dev.entities.features.FeatureType

class FeatureTypeIdMapper {

    private val map = mapOf(
        FeatureType.WATER_TRACKING to WATER_TRACKING_FEATURE_TYPE_ID,
        FeatureType.PEDOMETER to PEDOMETER_FEATURE_TYPE_ID,
        FeatureType.FASTING to FASTING_FEATURE_TYPE_ID,
        FeatureType.MOOD_TRACKING to MOOD_TRACKING_FEATURE_TYPE_ID,
        FeatureType.DIARY to DIARY_FEATURE_TYPE_ID,
    )

    fun mapToId(featureType: FeatureType) = checkNotNull(map[featureType])

    fun mapToFeatureType(featureTypeId: Int) = checkNotNull(
        map.entries.find { it.value == featureTypeId },
    ).key

    private companion object {
        private const val WATER_TRACKING_FEATURE_TYPE_ID = 0
        private const val PEDOMETER_FEATURE_TYPE_ID = 1
        private const val FASTING_FEATURE_TYPE_ID = 2
        private const val MOOD_TRACKING_FEATURE_TYPE_ID = 3
        private const val DIARY_FEATURE_TYPE_ID = 4
    }
}