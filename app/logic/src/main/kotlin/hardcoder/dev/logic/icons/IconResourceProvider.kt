package hardcoder.dev.logic.icons

interface IconResourceProvider {
    fun provideWaterTrackingIconResources(): List<String>
    fun provideMoodTrackingHobbyIconResources(): List<String>
    fun provideMoodTrackingMoodTypesIconsResources(): List<String>
}