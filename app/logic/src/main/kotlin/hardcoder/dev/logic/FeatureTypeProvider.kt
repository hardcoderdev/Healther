package hardcoder.dev.logic

import hardcoder.dev.logic.features.FeatureType
import kotlinx.coroutines.flow.flowOf

class FeatureTypeProvider {

    fun provide() = flowOf(FeatureType.values().toList())
}