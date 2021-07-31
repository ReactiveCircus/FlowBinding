plugins {
    `flowbinding-plugin`
    id("com.android.library")
    kotlin("android")
}

android.buildFeatures {
    viewBinding = true
    androidResources = true
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.androidx.fragment.ktx)
}
