package hardcoder.dev.presentation.dashboard.features.diary.tags

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.coroutines.combine
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.CorrectDiaryTagName
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTagDeleter
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTagNameValidator
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTagProvider
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.DiaryTagUpdater
import hardcoder.dev.logic.dashboard.features.diary.diaryTag.ValidatedDiaryTagName
import hardcoder.dev.logic.icons.IconResourceProvider
import hardcoder.dev.logic.icons.LocalIcon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UpdateTagViewModel(
    private val tagId: Int,
    private val diaryTagNameValidator: DiaryTagNameValidator,
    private val diaryTagDeleter: DiaryTagDeleter,
    private val diaryTagUpdater: DiaryTagUpdater,
    private val diaryTagProvider: DiaryTagProvider,
    iconResourceProvider: IconResourceProvider
) : ViewModel() {

    private val updateState = MutableStateFlow<UpdateState>(UpdateState.NotExecuted)
    private val deleteState = MutableStateFlow<DeleteState>(DeleteState.NotExecuted)
    private val tagName = MutableStateFlow<String?>(null)
    private val availableIconResourceList = MutableStateFlow(iconResourceProvider.getIcons())
    private val selectedIcon = MutableStateFlow(iconResourceProvider.getIcon(0))
    private val validatedTagName = tagName.map {
        it?.let {
            diaryTagNameValidator.validate(it)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    init {
        fillStateWithUpdatedTrack()
    }

    val state = combine(
        updateState,
        deleteState,
        tagName,
        validatedTagName,
        availableIconResourceList,
        selectedIcon
    ) { updateState, deleteState, tagName, validatedTagName,
        availableIconsList, iconId ->
        State(
            updateState = updateState,
            deleteState = deleteState,
            name = tagName,
            validatedName = validatedTagName,
            availableIconsList = availableIconsList,
            selectedIcon = iconId,
            allowUpdate = validatedTagName is CorrectDiaryTagName
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            updateState = updateState.value,
            deleteState = deleteState.value,
            name = tagName.value,
            validatedName = validatedTagName.value,
            availableIconsList = availableIconResourceList.value,
            selectedIcon = selectedIcon.value,
            allowUpdate = false
        )
    )

    fun updateTagName(name: String) {
        tagName.value = name
    }

    fun updateIcon(icon: LocalIcon) {
        selectedIcon.value = icon
    }

    fun updateTag() {
        viewModelScope.launch {
            val validatedName = validatedTagName.value
            require(validatedName is CorrectDiaryTagName)

            diaryTagProvider.provideDiaryTagById(tagId).firstOrNull()?.let {
                val updatedTrack = it.copy(
                    id = tagId,
                    name = validatedName.data,
                    icon = selectedIcon.value
                )
                diaryTagUpdater.update(updatedTrack)
                updateState.value = UpdateState.Executed
            } ?: throw Resources.NotFoundException("Tag not found by it's id")
        }
    }

    fun deleteById() {
        viewModelScope.launch {
            diaryTagDeleter.deleteById(tagId)
            deleteState.value = DeleteState.Executed
        }
    }

    private fun fillStateWithUpdatedTrack() {
        viewModelScope.launch {
            diaryTagProvider.provideDiaryTagById(tagId).firstOrNull()?.let { tag ->
                tagName.value = tag.name
                selectedIcon.value = tag.icon
            }
        }
    }

    data class State(
        val updateState: UpdateState,
        val deleteState: DeleteState,
        val allowUpdate: Boolean,
        val name: String?,
        val availableIconsList: List<LocalIcon>,
        val selectedIcon: LocalIcon,
        val validatedName: ValidatedDiaryTagName?
    )

    sealed class UpdateState {
        object Executed : UpdateState()
        object NotExecuted : UpdateState()
    }

    sealed class DeleteState {
        object Executed : DeleteState()
        object NotExecuted : DeleteState()
    }
}