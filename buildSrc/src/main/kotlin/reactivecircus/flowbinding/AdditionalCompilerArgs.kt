package reactivecircus.flowbinding

val additionalCompilerArgs = listOf(
    "-progressive",
    "-XXLanguage:+NewInference",
    "-Xuse-experimental=kotlin.Experimental"
)
