plugins {
    `flowbinding-plugin`
    id("com.android.library")
    kotlin("android")
}

android {
    lint {
        disable.add("FragmentGradleConfiguration")
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.material)

    api(libs.blueprint.testingRobot)
    api(libs.androidx.fragment.testing)
    api(libs.androidx.core)
    api(libs.androidx.test.monitor)
    api(libs.androidx.test.runner)
    api(libs.androidx.test.rules)
    api(libs.androidx.test.ext.junit)
    api(libs.androidx.espresso.core)
    api(libs.androidx.espresso.contrib)
    api(libs.androidx.espresso.intents)
    api(libs.truth)
}
