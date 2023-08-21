package hardcoder.dev.logic.reward.penalty

import hardcoder.dev.logic.features.FeatureType
import kotlinx.datetime.Instant

data class Penalty(
    val id: Int,
    val featureType: FeatureType,
    val date: Instant,
    val isCollected: Boolean,
)
