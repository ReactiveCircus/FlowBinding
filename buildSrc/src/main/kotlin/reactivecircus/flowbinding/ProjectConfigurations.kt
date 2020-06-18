package reactivecircus.flowbinding

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestedExtension
import org.gradle.StartParameter
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Configure root project.
 * Note that classpath dependencies still need to be defined in the `buildscript` block in the top-level build.gradle.kts file.
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
fun TestedExtension.configureCommonAndroidOptions(startParameter: StartParameter) {
    setCompileSdkVersion(androidSdk.compileSdk)
    buildToolsVersion(androidSdk.buildTools)

    defaultConfig.apply {
        // set minSdkVersion to 21 for android tests to avoid multi-dexing.
        val testTaskKeywords = listOf("androidTest", "connectedCheck")
        val isTestBuild = startParameter.taskNames.any { taskName ->
            testTaskKeywords.any { keyword ->
                taskName.contains(keyword, ignoreCase = true)
            }
        }
        if (!isTestBuild) {
            minSdkVersion(androidSdk.minSdk)
        } else {
            minSdkVersion(androidSdk.testMinSdk)
        }
        targetSdkVersion(androidSdk.targetSdk)

        // only support English for now
        resConfigs("en")
    }

    testOptions.animationsDisabled = true

    compileOptions(Action {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    })
}

/**
 * Apply configuration options for Android Library projects.
 */
@Suppress("UnstableApiUsage")
fun LibraryExtension.configureAndroidLibraryOptions(project: Project) {
    project.plugins.withType<KotlinAndroidPluginWrapper> {
        // disable unit test tasks if the unitTest source set is empty
        if (!project.hasUnitTestSource) {
            onVariants {
                unitTest { enabled = false }
            }
        }

        // disable android test tasks if the androidTest source set is empty
        if (!project.hasAndroidTestSource) {
            onVariants {
                androidTest { enabled = false }
            }
        }
    }

    packagingOptions {
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
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

    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        targetCompatibility = JavaVersion.VERSION_1_8.toString()
    }

    tasks.withType<KotlinJvmCompile>().configureEach {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + additionalCompilerArgs
        }
    }

    tasks.withType<Test>().configureEach {
        maxParallelForks = Runtime.getRuntime().availableProcessors() * 2
        testLogging {
            events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        }
    }
}

private val Project.hasUnitTestSource: Boolean
    get() {
        extensions.findByType(KotlinAndroidProjectExtension::class.java)?.sourceSets?.findByName("test")?.let {
            if (it.kotlin.files.isNotEmpty()) return true
        }
        extensions.findByType(KotlinProjectExtension::class.java)?.sourceSets?.findByName("test")?.let {
            if (it.kotlin.files.isNotEmpty()) return true
        }
        return false
    }

private val Project.hasAndroidTestSource: Boolean
    get() {
        extensions.findByType(KotlinAndroidProjectExtension::class.java)?.sourceSets?.findByName("androidTest")?.let {
            if (it.kotlin.files.isNotEmpty()) return true
        }
        return false
    }
