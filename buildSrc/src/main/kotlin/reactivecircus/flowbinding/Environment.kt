package reactivecircus.flowbinding

val isCiBuild: Boolean get() = System.getenv("CI") == "true"
