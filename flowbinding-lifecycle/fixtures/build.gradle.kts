plugins {
    `flowbinding-plugin`
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "reactivecircus.flowbinding.lifecycle.fixtures"
    buildFeatures {
        viewBinding = true
        androidResources = true
    }
}

dependencies {
    implementation(libs.androidx.lifecycle.commonJava8)
    implementation(libs.androidx.fragment.ktx)
}
