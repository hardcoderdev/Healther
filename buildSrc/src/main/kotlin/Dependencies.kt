import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {

    private object Versions {
        const val sqlDelight = "1.5.4"
        const val retrofit = "2.9.0"
        const val gsonConverter = "2.9.0"
        const val coroutines = "1.6.4"
        const val lifecycleCompose = "2.5.1"
        const val coreKtx = "1.9.0"
        const val navigation = "2.5.3"
        const val preferences = "1.2.0"
        const val playCore = "1.10.3"
        const val activityCompose = "1.6.1"
        const val composeBom = "2022.10.00"
        const val numberPicker = "1.0.3"
        const val composeCalendar = "1.0.2"
        const val composeMaterialDialogs = "0.8.1-rc"
        const val accompanistPermissions = "0.29.1-alpha"
        const val coreDesugaring = "2.0.0"
        const val dateTime = "0.4.0"
        const val composeCharts = "1.6.4"
    }

    const val sqlDelightCoroutinesExt = "com.squareup.sqldelight:coroutines-extensions:${Versions.sqlDelight}"
    const val sqlDelightDriver = "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}"
    const val preferences = "androidx.preference:preference-ktx:${Versions.preferences}"

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val gsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.gsonConverter}"

    const val coroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val playCore = "com.google.android.play:core:${Versions.playCore}"

    const val composeNavigation = "androidx.navigation:navigation-compose:${Versions.navigation}"
    const val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
    const val composeBom = "androidx.compose:compose-bom:${Versions.composeBom}"
    const val composeUi = "androidx.compose.ui:ui"
    const val composeGraphics = "androidx.compose.ui:ui-graphics"
    const val composeTooling = "androidx.compose.ui:ui-tooling-preview"
    const val debugTooling = "androidx.compose.ui:ui-tooling"
    const val composeMaterial = "androidx.compose.material3:material3"
    const val composeExtendedIcons = "androidx.compose.material:material-icons-extended"
    const val accompanistPermissions = "com.google.accompanist:accompanist-permissions:${Versions.accompanistPermissions}"

    const val lifecycleViewModelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycleCompose}"

    const val numberPicker = "com.chargemap.compose:numberpicker:${Versions.numberPicker}"
    const val composeCalendar =
        "io.github.boguszpawlowski.composecalendar:composecalendar:${Versions.composeCalendar}"
    const val composeCalendarDateTime =
        "io.github.boguszpawlowski.composecalendar:kotlinx-datetime:${Versions.composeCalendar}"
    const val composeMaterialDialogs =
        "io.github.vanpra.compose-material-dialogs:datetime:${Versions.composeMaterialDialogs}"
    const val composeCharts = "com.patrykandpatrick.vico:compose-m3:${Versions.composeCharts}"
    const val coreDesugaring = "com.android.tools:desugar_jdk_libs:${Versions.coreDesugaring}"
    const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.dateTime}"
}

fun DependencyHandler.addCommonAndroid() {
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.lifecycleViewModelKtx)
    implementation(Dependencies.playCore)
    coreLibraryDesugaring(Dependencies.coreDesugaring)
}

fun DependencyHandler.addData() {
    implementation(Dependencies.preferences)
    implementation(Dependencies.sqlDelightDriver)
    implementation(Dependencies.sqlDelightCoroutinesExt)
}

fun DependencyHandler.addCoroutines() {
    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.coroutinesAndroid)
}

fun DependencyHandler.addRetrofit() {
    implementation(Dependencies.retrofit)
    implementation(Dependencies.gsonConverter)
}

fun DependencyHandler.addCompose() {
    implementation(platform(Dependencies.composeBom))
    implementation(Dependencies.activityCompose)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeGraphics)
    implementation(Dependencies.composeMaterial)
    implementation(Dependencies.composeNavigation)
    implementation(Dependencies.composeTooling)
    implementation(Dependencies.composeExtendedIcons)
    debugImplementation(Dependencies.debugTooling)
    implementation(Dependencies.numberPicker)
    implementation(Dependencies.composeCalendar)
    implementation(Dependencies.composeCalendarDateTime)
    implementation(Dependencies.composeMaterialDialogs)
    implementation(Dependencies.composeCharts)
    implementation(Dependencies.accompanistPermissions)
}
