package reactivecircus.flowbinding

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import java.io.File

/**
 * Configure root project.
 * Note that classpath dependencies still need to be defined in the `buildscript` block in the top-level build.gradle file.
 */
fun Project.configureRootProject() {
    // apply detekt plugin to all sub-projects
    subprojects {
        it.applyDetektConfigs()
    }

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

    compileOptions {
        it.sourceCompatibility = JavaVersion.VERSION_1_8
        it.targetCompatibility = JavaVersion.VERSION_1_8
    }
}

/**
 * Apply configuration options for Android Library projects.
 */
fun LibraryExtension.configureAndroidLibraryOptions() {
    // Disable generating BuildConfig.java
    // TODO remove after https://issuetracker.google.com/72050365
    libraryVariants.all { variant ->
        variant.generateBuildConfigProvider.configure {
            it.enabled = false
        }
    }
}

/**
 * Apply configuration options for Android Application projects.
 */
fun AppExtension.configureAndroidApplicationOptions(project: Project) {
    packagingOptions.excludes = setOf(
        "kotlin/**",
        "**/*.kotlin_metadata",
        "META-INF/*.kotlin_module",
        "META-INF/*.version"
    )

    lintOptions.apply {
        disable("ParcelCreator")
        disable("GoogleAppIndexingWarning")
        isQuiet = false
        isIgnoreWarnings = false
        htmlReport = true
        xmlReport = true
        htmlOutput = File("${project.buildDir}/reports/lint/lint-reports.html")
        xmlOutput = File("${project.buildDir}/reports/lint/lint-reports.xml")
        isCheckDependencies = true
        isIgnoreTestSources = true
    }
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
