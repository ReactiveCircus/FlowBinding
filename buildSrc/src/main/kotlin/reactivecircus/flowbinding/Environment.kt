package reactivecircus.flowbinding

import org.gradle.api.Project

val Project.isCiBuild: Boolean
    get() = providers.environmentVariable("CI").orNull == "true"
