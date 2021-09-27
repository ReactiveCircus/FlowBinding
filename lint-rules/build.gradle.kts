plugins {
    `flowbinding-plugin`
    kotlin("jvm")
}

dependencies {
    compileOnly(libs.androidLint.lintApi)

    testImplementation(libs.junit)
    testImplementation(libs.androidLint.lint)
    testImplementation(libs.androidLint.lintTests)
}
