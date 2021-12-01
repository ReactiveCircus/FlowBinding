package reactivecircus.flowbinding

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType

/**
 * Apply detekt configs to the [Project].
 */
internal fun Project.configureDetektPlugin() {
    // apply detekt plugin
    pluginManager.apply(DetektPlugin::class.java)

    // enable Ktlint formatting
    val detektVersion = the<LibrariesForLibs>().versions.detekt.get()
    dependencies.add("detektPlugins", "io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")

    plugins.withType<DetektPlugin> {
        extensions.configure<DetektExtension> {
            source = files("src/")
            config = files("${project.rootDir}/detekt.yml")
            buildUponDefaultConfig = true
            allRules = true
        }
        tasks.withType<Detekt>().configureEach {
            reports {
                html.outputLocation.set(file("build/reports/detekt/${project.name}.html"))
            }
        }
    }
}
