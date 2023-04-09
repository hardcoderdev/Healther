package hardcoder.dev.logic.dashboard.features.diary.featureType

interface PredefinedFeatureTagProvider {
    fun providePredefined(): List<FeatureTagPredefined>
}

data class FeatureTagPredefined(val name: String)