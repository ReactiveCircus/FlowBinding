plugins {
    `flowbinding-plugin`
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "reactivecircus.flowbinding.preference.fixtures"
    buildFeatures {
        viewBinding = true
        androidResources = true
    }
}

dependencies {
    implementation(libs.androidx.preference)
}
