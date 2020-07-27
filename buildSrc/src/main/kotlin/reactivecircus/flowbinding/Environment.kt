package reactivecircus.flowbinding

import org.gradle.api.Project

@Suppress("UnstableApiUsage")
val Project.isCiBuild: Boolean
    get() = providers.environmentVariable("CI").forUseAtConfigurationTime().orNull == "true"
