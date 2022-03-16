plugins {
    `flowbinding-plugin`
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "reactivecircus.flowbinding.recyclerview.fixtures"
    buildFeatures {
        viewBinding = true
        androidResources = true
    }
}

dependencies {
    implementation(libs.androidx.recyclerView)
    implementation(libs.androidx.fragment.ktx)
}
