@file:Suppress("TooGenericExceptionCaught", "MaximumLineLength", "MaxLineLength")

package reactivecircus.kluentsuspend

import org.amshove.kluent.AnyException
import org.amshove.kluent.ExceptionResult
import org.amshove.kluent.NotThrowExceptionResult
import org.junit.ComparisonFailure
import kotlin.reflect.KClass

/**
 * Currently `invoking(...)` from Kluent doesn't support suspend functions.
 * This file adds an `invokingSuspend(...)` function and the corresponding assertions.
 *
 * TODO: replace with https://github.com/MarkusAmshove/Kluent/issues/151
 */
fun invokingSuspend(function: suspend () -> Any?): suspend () -> Any? = function

suspend infix fun <T : Throwable> (suspend () -> Any?).shouldThrow(expectedException: KClass<T>): ExceptionResult<T> {
    try {
        this.invoke()
        fail(
            "There was an Exception expected to be thrown, but nothing was thrown",
            "$expectedException",
            "None"
        )
    } catch (e: Throwable) {
        @Suppress("UNCHECKED_CAST")
        when {
            e.isA(ComparisonFailure::class) -> throw e
            e.isA(expectedException) -> return ExceptionResult(e as T)
            else -> throw ComparisonFailure(
                "Expected ${expectedException.javaObjectType} to be thrown",
                "${expectedException.javaObjectType}",
                "${e.javaClass}"
            )
        }
    }
}

suspend infix fun <T : Throwable> (suspend () -> Any?).shouldNotThrow(expectedException: KClass<T>): NotThrowExceptionResult {
    return try {
        this.invoke()
        NotThrowExceptionResult(noException)
    } catch (e: Throwable) {
        if (expectedException.isAnyException()) {
            fail(
                "Expected no Exception to be thrown",
                "No Exception",
                "${e.javaClass}"
            )
        }
        if (e.isA(expectedException)) {
            fail(
                "Expected no Exception of type ${e::class.qualifiedName} to be thrown",
                "No Exception",
                e.toInformativeString()
            )
        }
        NotThrowExceptionResult(e)
    }
}

suspend infix fun <T : Throwable> (suspend () -> Any?).shouldThrow(expectedException: T) {
    try {
        this.invoke()
        fail(
            "There was an Exception expected to be thrown, but nothing was thrown",
            "$expectedException",
            "None"
        )
    } catch (e: Throwable) {
        if (e != expectedException) {
            throw ComparisonFailure(
                "Expected $expectedException to be thrown",
                "$expectedException",
                "${e.javaClass}"
            )
        }
    }
}

internal val noException = Exception("None")
internal fun fail(message: String, expected: String, actual: String): Nothing =
    throw ComparisonFailure(message, expected, actual)

internal fun Throwable.isA(expected: KClass<out Throwable>) =
    expected.isAnyException() || expected.java.isAssignableFrom(this.javaClass)

internal fun <T : Throwable> KClass<T>.isAnyException() =
    this.javaObjectType == AnyException.javaObjectType

private fun Throwable.toInformativeString() = when (this.message) {
    null -> "${this::class.qualifiedName}"
    else -> "${this::class.qualifiedName} (Message: \"${this.message}\")"
}
