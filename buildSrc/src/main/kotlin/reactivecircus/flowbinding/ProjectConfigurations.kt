package reactivecircus.flowbinding

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

/**
 * Configure root project.
 * Note that classpath dependencies still need to be defined in the `buildscript` block in the top-level build.gradle file.
 */
fun Project.configureRootProject() {
    // register task for cleaning the build directory in the root project
    tasks.register("clean", Delete::class.java) {
        delete(rootProject.buildDir)
    }
}

/**
 * Apply common configurations for all Android projects (Application and Library).
 */
fun BaseExtension.configureCommonAndroidOptions() {
    setCompileSdkVersion(androidSdk.compileSdk)
    buildToolsVersion(androidSdk.buildTools)

    defaultConfig.apply {
        minSdkVersion(androidSdk.minSdk)
        targetSdkVersion(androidSdk.targetSdk)

        // only support English for now
        resConfigs("en")
    }

    testOptions.animationsDisabled = true

    dexOptions.preDexLibraries = !isCiBuild

    compileOptions(Action {
        it.sourceCompatibility = JavaVersion.VERSION_1_8
        it.targetCompatibility = JavaVersion.VERSION_1_8
    })

    packagingOptions.apply {
        exclude("kotlin/**")
        exclude("**/*.kotlin_metadata")
        exclude("META-INF/*.kotlin_module")
        exclude("META-INF/*.version")
    }
}

/**
 * Apply configuration options for Android Library projects.
 */
@Suppress("UnstableApiUsage")
fun LibraryExtension.configureAndroidLibraryOptions() {
    // Disable generating BuildConfig.java
    // TODO disable buildConfig once androidTest works without it
    buildFeatures.buildConfig = true
}

/**
 * Apply common configurations for all projects (including the root project).
 */
fun Project.configureForAllProjects() {
    repositories.apply {
        mavenCentral()
        google()
        jcenter()
    }

    tasks.withType(JavaCompile::class.java).configureEach { task ->
        task.sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        task.targetCompatibility = JavaVersion.VERSION_1_8.toString()
    }

    tasks.withType(KotlinJvmCompile::class.java).configureEach { task ->
        task.kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    tasks.withType(KotlinCompile::class.java).configureEach { task ->
        task.kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + additionalCompilerArgs
        }
    }

    tasks.withType(Test::class.java).configureEach { test ->
        test.maxParallelForks = Runtime.getRuntime().availableProcessors() * 2
        test.testLogging {
            it.events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        }
    }
}
