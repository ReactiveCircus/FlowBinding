package reactivecircus.flowbinding

import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.DokkaPlugin

/**
 * Apply and configure the [DokkaPlugin] for the [Project].
 */
internal fun Project.configureDokka() {
    pluginManager.apply(DokkaPlugin::class.java)
    tasks.withType<DokkaMultiModuleTask>().configureEach {
        val apiDir = rootDir.resolve("docs/api")
        outputDirectory.set(apiDir)
        doLast {
            apiDir.resolve("-modules.html").renameTo(apiDir.resolve("index.html"))
        }
    }
}
