package hardcoder.dev.logic.dashboard.features.diary.diaryTrack

class DiaryTrackTitleValidator {

    fun validate(title: String) = title.incorrectReason()?.let { reason ->
        IncorrectValidatedDiaryTrackTitle(data = title, reason = reason)
    } ?: run {
        CorrectValidatedDiaryTrackTitle(data = title)
    }

    private fun String.incorrectReason(): IncorrectValidatedDiaryTrackTitle.Reason? {
        return when {
            length > DIARY_TRACK_TITLE_MAX_CHARS -> IncorrectValidatedDiaryTrackTitle.Reason.MoreThanMaxChars(
                DIARY_TRACK_TITLE_MAX_CHARS
            )
            else -> null
        }
    }

    private companion object {
        private const val DIARY_TRACK_TITLE_MAX_CHARS = 80
    }
}

sealed class ValidatedDiaryTrackTitle {
    abstract val data: String
}

data class CorrectValidatedDiaryTrackTitle(
    override val data: String
) : ValidatedDiaryTrackTitle()

data class IncorrectValidatedDiaryTrackTitle(
    override val data: String,
    val reason: Reason
) : ValidatedDiaryTrackTitle() {
    sealed class Reason {
        data class MoreThanMaxChars(val maxChars: Int) : Reason()
    }
}