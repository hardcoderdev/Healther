package hardcoder.dev.logic.dashboard.features.diary.diaryWithFeatureType

import hardcoder.dev.logic.dashboard.features.diary.diaryTrack.DiaryTrack
import hardcoder.dev.logic.dashboard.features.diary.featureTag.FeatureTag

data class DiaryWithFeatureTags(
    val diaryTrack: DiaryTrack,
    val featureTags: List<FeatureTag>
)