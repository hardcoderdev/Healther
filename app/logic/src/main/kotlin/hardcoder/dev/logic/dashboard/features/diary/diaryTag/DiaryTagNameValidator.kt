package hardcoder.dev.logic.dashboard.features.diary.diaryTag

class DiaryTagNameValidator {

    fun validate(name: String) = name.incorrectReason()?.let { reason ->
        IncorrectValidatedDiaryTagName(data = name, reason = reason)
    } ?: run {
        CorrectValidatedDiaryTagName(data = name)
    }

    private fun String.incorrectReason(): IncorrectValidatedDiaryTagName.Reason? {
        return when {
            isEmpty() -> IncorrectValidatedDiaryTagName.Reason.Empty
            length > DIARY_TAG_NAME_MAX_CHARS -> IncorrectValidatedDiaryTagName.Reason.MoreThanMaxChars(
                DIARY_TAG_NAME_MAX_CHARS
            )
            else -> null
        }
    }

    private companion object {
        private const val DIARY_TAG_NAME_MAX_CHARS = 20
    }
}

sealed class ValidatedDiaryTagName {
    abstract val data: String
}

data class CorrectValidatedDiaryTagName(
    override val data: String
) : ValidatedDiaryTagName()

data class IncorrectValidatedDiaryTagName(
    override val data: String,
    val reason: Reason
) : ValidatedDiaryTagName() {
    sealed class Reason {
        object Empty : Reason()
        data class MoreThanMaxChars(val maxChars: Int) : Reason()
    }
}