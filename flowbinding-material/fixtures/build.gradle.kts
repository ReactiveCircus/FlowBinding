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
    implementation(libs.material)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.coordinatorLayout)
}
