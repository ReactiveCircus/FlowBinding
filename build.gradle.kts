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
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${versions.getValue("detekt")}")
        classpath("com.vanniktech:gradle-maven-publish-plugin:${versions.getValue("mavenPublishPlugin")}")
        classpath("io.github.reactivecircus.firestorm:firestorm-gradle-plugin:${versions.getValue("firestormGradlePlugin")}")
    }
}

plugins {
    `flowbinding-plugin`
    id("org.jetbrains.dokka") version "1.4.10"
}

tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(rootDir.resolve("docs/api"))
}

subprojects {
    apply(from = "${project.rootDir}/gradle/detekt.gradle")
}
