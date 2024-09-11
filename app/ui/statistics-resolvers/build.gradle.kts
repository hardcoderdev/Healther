plugins {
    id("android-library-convention")
}

dependencies {
    api(projects.app.domain.logics)
    api(projects.app.ui.formatters)
 