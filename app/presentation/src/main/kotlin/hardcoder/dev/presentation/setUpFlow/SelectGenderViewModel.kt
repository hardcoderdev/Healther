package hardcoder.dev.presentation.setUpFlow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hardcoder.dev.controller.SingleSelectionController
import hardcoder.dev.logic.hero.gender.GenderProvider

class SelectGenderViewModel(genderProvider: GenderProvider) : ViewModel() {

    val genderSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = genderProvider.provideAllGenders()
    )
}