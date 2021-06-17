@file:Suppress("unused")

package reactivecircus.flowbinding

import com.android.build.gradle.LibraryPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.getByType

/**
 * A plugin that provides baseline gradle configurations for all projects, including:
 * - root project
 * - Android Application projects
 * - Android Library projects
 * - Kotlin JVM projects
 * - Java JVM projects
 *
 * Apply this plugin to the build.gradle.kts file in all projects:
 * ```
 * plugins {
 *     id 'flowbinding-plugin'
 * }
 * ```
 */
@ExperimentalStdlibApi
class FlowBindingPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val flowBindingExtension = project.extensions.create("flowBinding", FlowBindingExtension::class.java)

        project.configureForAllProjects(flowBindingExtension.enableExplicitApi)

        if (project.isRoot) {
            project.configureRootProject()
        }

        project.plugins.all {
            when (this) {
                is JavaPlugin,
                is JavaLibraryPlugin -> {
                    project.extensions.getByType<JavaPluginExtension>().apply {
                        sourceCompatibility = JavaVersion.VERSION_11
                        targetCompatibility = JavaVersion.VERSION_11
                    }
                }
                is LibraryPlugin -> {
                    project.configureAndroidLibrary(project.gradle.startParameter)
                }
            }
        }
    }
}

val Project.isRoot get() = this == this.rootProject
