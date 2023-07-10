package hardcoder.dev.logic.features.diary.diaryTag

class DiaryTagNameValidator {

    fun validate(name: String) = name.incorrectReason()?.let { reason ->
        IncorrectDiaryTagName(data = name, reason = reason)
    } ?: run {
        CorrectDiaryTagName(data = name)
    }

    private fun String.incorrectReason(): IncorrectDiaryTagName.Reason? {
        return when {
            isEmpty() -> IncorrectDiaryTagName.Reason.Empty
            length > DIARY_TAG_NAME_MAX_CHARS -> IncorrectDiaryTagName.Reason.MoreThanMaxChars(
                DIARY_TAG_NAME_MAX_CHARS,
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

data class CorrectDiaryTagName(
    override val data: String,
) : ValidatedDiaryTagName()

data class IncorrectDiaryTagName(
    override val data: String,
    val reason: Reason,
) : ValidatedDiaryTagName() {
    sealed class Reason {
        object Empty : Reason()
        data class MoreThanMaxChars(val maxChars: Int) : Reason()
    }
}