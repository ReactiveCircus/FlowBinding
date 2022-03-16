plugins {
    `flowbinding-plugin`
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "reactivecircus.flowbinding.viewpager.fixtures"
    buildFeatures {
        viewBinding = true
        androidResources = true
    }
}


dependencies {
    implementation(libs.androidx.viewPager)
    implementation(libs.androidx.fragment.ktx)
}
