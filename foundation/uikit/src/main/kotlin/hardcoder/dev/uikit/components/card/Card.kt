package hardcoder.dev.uikit.components.card

import androidx.compose.runtime.Composable
import hardcoder.dev.uikit.components.card.internal.ActionCard
import hardcoder.dev.uikit.components.card.internal.ActionCardPreview
import hardcoder.dev.uikit.components.card.internal.SelectionCard
import hardcoder.dev.uikit.components.card.internal.SelectionCardPreview
import hardcoder.dev.uikit.components.card.internal.StaticCard
import hardcoder.dev.uikit.components.card.internal.StaticCardPreview
import hardcoder.dev.uikit.preview.UiKitPhonePreview

@Composable
fun Card(cardConfig: CardConfig) {
    when (cardConfig) {
        is CardConfig.Action -> ActionCard(
            modifier = cardConfig.modifier,
            onClick = cardConfig.onClick,
            cardContent = cardConfig.cardContent,
        )

        is CardConfig.Selection -> SelectionCard(
            modifier = cardConfig.modifier,
            onSelect = cardConfig.onSelect,
            isSelected = cardConfig.isSelected,
            cardContent = cardConfig.cardContent,
        )

        is CardConfig.Static -> StaticCard(
            cardContent = cardConfig.cardContent,
        )
    }
}

@UiKitPhonePreview
@Composable
private fun ActionCardPreview() {
    ActionCardPreview()
}

@UiKitPhonePreview
@Composable
private fun SelectionCardPreview() {
    SelectionCardPreview()
}

@UiKitPhonePreview
@Composable
private fun StaticCardPreview() {
    StaticCardPreview()
}