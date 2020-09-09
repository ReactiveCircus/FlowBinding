package reactivecircus.flowbinding

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import kotlinx.validation.ApiValidationExtension
import kotlinx.validation.BinaryCompatibilityValidatorPlugin

/**
 * Apply and configure the [BinaryCompatibilityValidatorPlugin] for the [Project].
 */
internal fun Project.configureBinaryCompatibilityValidation() {
    pluginManager.apply(BinaryCompatibilityValidatorPlugin::class.java)
    plugins.withType<BinaryCompatibilityValidatorPlugin> {
        extensions.configure<ApiValidationExtension> {
            ignoredProjects.addAll(IGNORED_PROJECTS)
        }
    }
}

private val IGNORED_PROJECTS = listOf(
    "flowbinding-activity-fixtures",
    "flowbinding-android-fixtures",
    "flowbinding-appcompat-fixtures",
    "flowbinding-core-fixtures",
    "flowbinding-drawerlayout-fixtures",
    "flowbinding-lifecycle-fixtures",
    "flowbinding-material-fixtures",
    "flowbinding-navigation-fixtures",
    "flowbinding-preference-fixtures",
    "flowbinding-recyclerview-fixtures",
    "flowbinding-swiperefreshlayout-fixtures",
    "flowbinding-viewpager-fixtures",
    "flowbinding-viewpager2-fixtures",
    "testing-infra",
    "lint-rules"
)
