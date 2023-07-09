plugins {
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    parallel = true
    config.setFrom(rootProject.rootDir.path + "/detekt.yml")
    buildUponDefaultConfig = true
}