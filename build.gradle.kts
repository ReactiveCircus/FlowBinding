buildscript {
    apply(from = "buildSrc/dependencies.gradle")
    val versions: Map<Any, Any> by extra

    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${versions.getValue("androidGradlePlugin")}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${versions.getValue("kotlin")}")
        classpath("org.jetbrains.kotlinx:binary-compatibility-validator:${versions.getValue("binaryCompatibilityValidator")}")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${versions.getValue("detekt")}")
        classpath("com.vanniktech:gradle-maven-publish-plugin:${versions.getValue("mavenPublishPlugin")}")
        classpath("io.github.reactivecircus.firestorm:firestorm-gradle-plugin:${versions.getValue("firestormGradlePlugin")}")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:${versions.getValue("dokka")}")
    }
}

plugins {
    `flowbinding-plugin`
}

apply(plugin = "binary-compatibility-validator")

configure<kotlinx.validation.ApiValidationExtension> {
    ignoredProjects.addAll(
        listOf("fixtures", "testing-infra", "lint-rules")
    )
}

subprojects {
    apply(from = "${project.rootDir}/gradle/detekt.gradle")
}
