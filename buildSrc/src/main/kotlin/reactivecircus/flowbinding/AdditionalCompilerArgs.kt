package reactivecircus.flowbinding

val additionalCompilerArgs = listOf(
    "-progressive",
    "-XXLanguage:+NewInference",
    "-Xopt-in=kotlin.Experimental"
)
