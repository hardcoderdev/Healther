import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {

    private object Versions {
        const val room = "2.5.0"
        const val retrofit = "2.9.0"
        const val gsonConverter = "2.9.0"
        const val coroutines = "1.6.4"
        const val lifecycleCompose = "2.5.1"
        const val coreKtx = "1.9.0"
        const val navigation = "2.5.3"
        const val preferences = "1.2.0"
        const val playCore = "1.10.3"
        const val dataStore = "1.0.0"
        const val activityCompose = "1.6.1"
        const val composeBom = "2022.10.00"
    }

    const val room = "androidx.room:room-runtime:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val gsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.gsonConverter}"

    const val coroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val playCore = "com.google.android.play:core:${Versions.playCore}"

    const val preferences = "androidx.preference:preference-ktx:${Versions.preferences}"
    const val dataStore = "androidx.datastore:datastore:${Versions.dataStore}"

    const val composeNavigation = "androidx.navigation:navigation-compose:${Versions.navigation}"
    const val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
    const val composeBom = "androidx.compose:compose-bom:${Versions.composeBom}"
    const val composeUi = "androidx.compose.ui:ui"
    const val composeGraphics = "androidx.compose.ui:ui-graphics"
    const val composeTooling = "androidx.compose.ui:ui-tooling-preview"
    const val composeMaterial = "androidx.compose.material3:material3"
    const val lifecycleViewModelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycleCompose}"
}

fun DependencyHandler.addCommonAndroid() {
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.lifecycleViewModelKtx)
    implementation(Dependencies.playCore)
}

fun DependencyHandler.addData() {
    implementation(Dependencies.preferences)
    implementation(Dependencies.dataStore)
    implementation(Dependencies.room)
    implementation(Dependencies.roomKtx)
    kapt(Dependencies.roomCompiler)
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
    implementation(Dependencies.composeTooling)
    implementation(Dependencies.composeNavigation)
}