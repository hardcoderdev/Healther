package hardcoder.dev.logic.dashboard.features.diary.featureTag

interface PredefinedFeatureTagProvider {
    fun providePredefined(): List<FeatureTagPredefined>
}

data class FeatureTagPredefined(val name: String)