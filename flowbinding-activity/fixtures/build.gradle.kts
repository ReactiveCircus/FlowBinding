plugins {
    `flowbinding-plugin`
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "reactivecircus.flowbinding.activity.fixtures"
    buildFeatures {
        viewBinding = true
        androidResources = true
    }
}

dependencies {
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment.ktx)
}
