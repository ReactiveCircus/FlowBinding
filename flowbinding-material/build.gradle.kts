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
    namespace = "reactivecircus.flowbinding.material"
    defaultConfig {
        testApplicationId = "reactivecircus.flowbinding.material.test"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    api(project(":flowbinding-common"))

    implementation(libs.material)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.kotlinx.coroutines.android)

    lintChecks(project(":lint-rules"))

    androidTestImplementation(project(":testing-infra"))
    androidTestImplementation(project(":flowbinding-material-fixtures"))
}
