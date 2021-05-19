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
    }
}

plugins {
    `flowbinding-plugin`
    id("org.jetbrains.dokka") version "1.4.32"
}

tasks.dokkaHtmlMultiModule.configure {
    val apiDir = rootDir.resolve("docs/api")
    outputDirectory.set(apiDir)
    doLast {
        apiDir.resolve("-modules.html").renameTo(apiDir.resolve("index.html"))
    }
}

subprojects {
    apply(from = "${project.rootDir}/gradle/detekt.gradle")
}
