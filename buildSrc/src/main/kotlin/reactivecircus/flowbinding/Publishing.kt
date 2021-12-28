package reactivecircus.flowbinding

import com.vanniktech.maven.publish.MavenPublishPlugin
import com.vanniktech.maven.publish.MavenPublishPluginExtension
import com.vanniktech.maven.publish.SonatypeHost
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

/**
 * Configure the [MavenPublishPlugin] if applied.
 */
internal fun Project.configureMavenPublishing() {
    plugins.withType<MavenPublishPlugin> {
        extensions.configure<MavenPublishPluginExtension> {
            sonatypeHost = SonatypeHost.S01
        }
    }
}
