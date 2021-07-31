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
    // TODO remove once https://issuetracker.google.com/issues/161586464 is fixed
    buildFeatures.androidResources = true

    defaultConfig {
        testApplicationId = "reactivecircus.flowbinding.common.test"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    api(libs.kotlinx.coroutines.android)
    api(libs.androidx.annotation)
    debugRuntimeOnly(libs.androidx.fragment.ktx)

    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}
