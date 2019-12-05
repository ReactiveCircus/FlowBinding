@file:Suppress("unused")

package reactivecircus.flowbinding

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention

/**
 * A plugin that provides baseline gradle configurations for all projects, including:
 * - root project
 * - Android Application projects
 * - Android Library projects
 * - Kotlin JVM projects
 * - Java JVM projects
 *
 * Apply this plugin to the build.gradle file in all projects:
 * ```
 * plugins {
 *     id 'flowbinding-plugin'
 * }
 * ```
 */
class FlowBindingPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.afterEvaluate {
            project.configureForAllProjects()

            if (project.isRoot) {
                project.configureRootProject()
            }

            project.plugins.all { plugin ->
                when (plugin) {
                    is JavaPlugin,
                    is JavaLibraryPlugin -> {
                        project.convention.getPlugin(JavaPluginConvention::class.java).apply {
                            sourceCompatibility = JavaVersion.VERSION_1_8
                            targetCompatibility = JavaVersion.VERSION_1_8
                        }
                    }
                    is LibraryPlugin -> {
                        project.libraryExtension.configureCommonAndroidOptions()
                        project.libraryExtension.configureAndroidLibraryOptions()
                    }
                    is AppPlugin -> {
                        project.appExtension.configureCommonAndroidOptions()
                    }
                }
            }
        }
    }
}

val Project.isRoot get() = this == this.rootProject
val Project.appExtension: AppExtension get() = extensions.getByType(AppExtension::class.java)
val Project.libraryExtension: LibraryExtension get() = extensions.getByType(LibraryExtension::class.java)
