plugins {
    `flowbinding-plugin`
    kotlin("jvm")
}

dependencies {
    compileOnly(libs.androidLint.lintApi)

    testImplementation(libs.junit)
    testImplementation(libs.androidLint.lint)
    testImplementation(libs.androidLint.lintTests)
    testImplementation(libs.androidLint.testUtils)
}

tasks.jar {
    manifest {
        attributes("Lint-Registry-v2" to "reactivecircus.flowbinding.lint.FlowBindingIssueRegistry")
    }
}
