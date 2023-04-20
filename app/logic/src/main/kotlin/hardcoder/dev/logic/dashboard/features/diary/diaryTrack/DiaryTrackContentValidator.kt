package hardcoder.dev.logic.dashboard.features.diary.diaryTrack

class DiaryTrackContentValidator {

    fun validate(content: String) = content.incorrectReason()?.let { reason ->
        IncorrectDiaryTrackContent(data = content, reason = reason)
    } ?: run {
        CorrectDiaryTrackContent(data = content)
    }

    private fun String.incorrectReason(): IncorrectDiaryTrackContent.Reason? {
        return when {
            isEmpty() -> IncorrectDiaryTrackContent.Reason.Empty
            else -> null
        }
    }
}

sealed class ValidatedDiaryTrackContent {
    abstract val data: String
}

data class CorrectDiaryTrackContent(
    override val data: String
) : ValidatedDiaryTrackContent()

data class IncorrectDiaryTrackContent(
    override val data: String,
    val reason: Reason
) : ValidatedDiaryTrackContent() {
    sealed class Reason {
        object Empty : Reason()
    }
}