package hardcoder.dev.logic.features.moodTracking.hobby

class HobbyIconValidator {

    fun validate(hobbyIconResource: HobbyIconResource): ValidateHobbyIcon {
        return hobbyIconResource.incorrectReason()?.let { reason ->
            IncorrectHobbyIcon(data = hobbyIconResource, reason = reason)
        } ?: run {
            CorrectHobbyIcon(data = hobbyIconResource)
        }
    }

    private fun HobbyIconResource.incorrectReason(): IncorrectHobbyIcon.Reason? {
        return when {
            value.isEmpty() -> IncorrectHobbyIcon.Reason.NotFound
            else -> null
        }
    }
}

sealed class ValidateHobbyIcon {
    abstract val data: HobbyIconResource
}

data class CorrectHobbyIcon(override val data: HobbyIconResource) : ValidateHobbyIcon()
data class IncorrectHobbyIcon(
    override val data: HobbyIconResource,
    val reason: Reason
) : ValidateHobbyIcon() {
    sealed class Reason {
        object NotFound : Reason()
    }
}

data class HobbyIconResource(val value: String)