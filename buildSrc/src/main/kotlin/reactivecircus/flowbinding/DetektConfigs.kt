package reactivecircus.flowbinding

import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.detekt
import org.gradle.api.Project

/**
 * Apply the detekt plugin with configurations.
 */
fun Project.applyDetektConfigs() {
    pluginManager.apply(DetektPlugin::class.java)
    detekt {
        input = files("src/")
        failFast = true
        parallel = true
        config = files("${project.rootDir}/detekt.yml")
        reports.html.destination = file("${project.buildDir}/reports/detekt/${project.name}.html")
    }
}
