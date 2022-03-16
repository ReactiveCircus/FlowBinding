plugins {
    `flowbinding-plugin`
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "reactivecircus.flowbinding.drawerlayout.fixtures"
    buildFeatures {
        viewBinding = true
        androidResources = true
    }
}

dependencies {
    implementation(libs.androidx.drawerLayout)
    implementation(libs.androidx.fragment.ktx)
}
