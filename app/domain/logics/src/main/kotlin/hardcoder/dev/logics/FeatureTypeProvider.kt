package hardcoder.dev.logics

import hardcoder.dev.entities.features.FeatureType
import kotlinx.coroutines.flow.flowOf

class FeatureTypeProvider {

    fun provide() = flowOf(FeatureType.entries)
}