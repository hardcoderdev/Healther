import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.implementation(depName: Any) {
    add("implementation", depName)
}

fun DependencyHandler.debugImplementation(depName: Any) {
    add("debugImplementation", depName)
}

fun DependencyHandler.testImplementation(depName: Any) {
    add("testImplementation", depName)
}

fun DependencyHandler.androidTestImplementation(depName: Any) {
    add("androidTestImplementation", depName)
}

fun DependencyHandler.kapt(depName: Any) {
    add("kapt", depName)
}

fun DependencyHandler.ksp(depName: Any) {
    add("ksp", depName)
}