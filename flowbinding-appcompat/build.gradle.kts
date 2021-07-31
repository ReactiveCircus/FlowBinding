plugins {
    `flowbinding-plugin`
    id("com.android.library")
    kotlin("android")
    id("com.vanniktech.maven.publish")
    id("org.jetbrains.dokka")
}

flowBinding {
    enableExplicitApi.set(true)
}

android {
    defaultConfig {
        testApplicationId = "reactivecircus.flowbinding.appcompat.test"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    api(project(":flowbinding-common"))

    implementation(libs.androidx.appCompat)
    implementation(libs.kotlinx.coroutines.android)

    lintChecks(project(":lint-rules"))

    androidTestImplementation(project(":testing-infra"))
    androidTestImplementation(project(":flowbinding-appcompat-fixtures"))
}
