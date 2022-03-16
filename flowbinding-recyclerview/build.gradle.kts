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
    namespace = "reactivecircus.flowbinding.recyclerview"
    defaultConfig {
        testApplicationId = "reactivecircus.flowbinding.recyclerview.test"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    api(project(":flowbinding-common"))

    implementation(libs.androidx.recyclerView)
    implementation(libs.kotlinx.coroutines.android)

    lintChecks(project(":lint-rules"))

    androidTestImplementation(project(":testing-infra"))
    androidTestImplementation(project(":flowbinding-recyclerview-fixtures"))
}
