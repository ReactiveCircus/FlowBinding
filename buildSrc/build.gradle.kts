apply(from = "dependencies.gradle")
val versions: Map<Any, Any> by extra

plugins {
    `kotlin-dsl`
}

repositories {
    google()
    gradlePluginPortal()
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${versions.getValue("kotlin")}")
    implementation("com.android.tools.build:gradle:${versions.getValue("androidGradlePlugin")}")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${versions.getValue("detekt")}")
}

gradlePlugin {
    plugins {
        register("flowbinding") {
            id = "flowbinding-plugin"
            implementationClass = "reactivecircus.flowbinding.FlowBindingPlugin"
        }
    }
}
