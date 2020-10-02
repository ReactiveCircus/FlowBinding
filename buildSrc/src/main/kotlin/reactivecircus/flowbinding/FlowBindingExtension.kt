package reactivecircus.flowbinding

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property

/**
 * Extension for [FlowBindingPlugin].
 */
@Suppress("UnstableApiUsage", "unused")
open class FlowBindingExtension internal constructor(objects: ObjectFactory) {

    /**
     * Whether to enable strict explicit API mode for the project.
     *
     * Default is `false`.
     */
    val enableExplicitApi: Property<Boolean> = objects.property<Boolean>().convention(false)
}
