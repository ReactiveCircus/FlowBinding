rootProject.name = "FlowBinding"

include(":flowbinding-common")

include(":flowbinding-android")
includeProject(":flowbinding-android:fixtures", "flowbinding-android/fixtures")

include(":flowbinding-activity")
includeProject(":flowbinding-activity:fixtures", "flowbinding-activity/fixtures")

include(":flowbinding-appcompat")
includeProject(":flowbinding-appcompat:fixtures", "flowbinding-appcompat/fixtures")

include(":flowbinding-core")
includeProject(":flowbinding-core:fixtures", "flowbinding-core/fixtures")

include(":flowbinding-drawerlayout")
includeProject(":flowbinding-drawerlayout:fixtures", "flowbinding-drawerlayout/fixtures")

include(":flowbinding-lifecycle")
includeProject(":flowbinding-lifecycle:fixtures", "flowbinding-lifecycle/fixtures")

include(":flowbinding-material")
includeProject(":flowbinding-material:fixtures", "flowbinding-material/fixtures")

include(":flowbinding-navigation")
includeProject(":flowbinding-navigation:fixtures", "flowbinding-navigation/fixtures")

include(":flowbinding-preference")
includeProject(":flowbinding-preference:fixtures", "flowbinding-preference/fixtures")

include(":flowbinding-recyclerview")
includeProject(":flowbinding-recyclerview:fixtures", "flowbinding-recyclerview/fixtures")

include(":flowbinding-swiperefreshlayout")
includeProject(":flowbinding-swiperefreshlayout:fixtures", "flowbinding-swiperefreshlayout/fixtures")

include(":flowbinding-viewpager2")
includeProject(":flowbinding-viewpager2:fixtures", "flowbinding-viewpager2/fixtures")

include(":testing-infra")

include(":lint-rules")

fun includeProject(name: String, filePath: String) {
    include(name)
    project(name).projectDir = File(filePath)
}

// apply build cache configuration
apply(from = File(settingsDir, "gradle/buildCacheSettings.gradle"))
