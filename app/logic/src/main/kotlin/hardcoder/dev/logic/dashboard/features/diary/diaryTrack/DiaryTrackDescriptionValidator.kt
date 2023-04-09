package hardcoder.dev.logic.dashboard.features.diary.diaryTrack

class DiaryTrackDescriptionValidator {

    fun validate(description: String) = description.incorrectReason()?.let { reason ->
        IncorrectValidatedDiaryTrackDescription(data = description, reason = reason)
    } ?: run {
        CorrectValidatedDiaryTrackDescription(data = description)
    }

    private fun String.incorrectReason(): IncorrectValidatedDiaryTrackDescription.Reason? {
        return when {
            isEmpty() -> IncorrectValidatedDiaryTrackDescription.Reason.Empty
            else -> null
        }
    }
}

sealed class ValidatedDiaryTrackDescription {
    abstract val data: String
}

data class CorrectValidatedDiaryTrackDescription(
    override val data: String
) : ValidatedDiaryTrackDescription()

data class IncorrectValidatedDiaryTrackDescription(
    override val data: String,
    val reason: Reason
) : ValidatedDiaryTrackDescription() {
    sealed class Reason {
        object Empty : Reason()
    }
}