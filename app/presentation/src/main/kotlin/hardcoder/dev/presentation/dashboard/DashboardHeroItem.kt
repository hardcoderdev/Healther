package hardcoder.dev.presentation.dashboard

import hardcoder.dev.logic.hero.Hero

sealed class DashboardHeroItem {
    data class HeroSection(
        val hero: Hero,
        val healthPointsProgress: Float,
        val experiencePointsProgress: Float,
        val experiencePointsToNextLevel: Float,
    ) : DashboardHeroItem()
}