package hardcoder.dev.logic.features.general

class IconResourceValidator {

    fun validate(data: IconResource) = data.incorrectReason()?.let { reason ->
        IncorrectValidatedIconResource(data = data, reason = reason)
    } ?: run {
        CorrectValidatedIconResource(data = data)
    }

    private fun IconResource.incorrectReason(): IncorrectValidatedIconResource.Reason? {
        return when {
            value.isEmpty() -> IncorrectValidatedIconResource.Reason.NotSelected
            else -> null
        }
    }
}

sealed class ValidatedIconResource {
    abstract val data: IconResource
}

data class CorrectValidatedIconResource(
    override val data: IconResource
) : ValidatedIconResource()

data class IncorrectValidatedIconResource(
    override val data: IconResource,
    val reason: Reason
) : ValidatedIconResource() {
    sealed class Reason {
        object NotSelected : Reason()
    }
}

data class IconResource(val value: String)