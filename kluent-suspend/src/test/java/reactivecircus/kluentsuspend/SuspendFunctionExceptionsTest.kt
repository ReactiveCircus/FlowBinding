@file:Suppress("TooGenericExceptionThrown", "UnderscoresInNumericLiterals")

package reactivecircus.kluentsuspend

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.AnyException
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.withCause
import org.amshove.kluent.withMessage
import org.junit.Test
import java.io.IOException
import kotlin.test.assertFails

@ExperimentalCoroutinesApi
class SuspendFunctionExceptionsTest {

    @Test
    fun passWhenTestingASuspendFunctionThatThrowsTheExpectedException() = runBlockingTest {
        suspend fun func() {
            suspendCancellableCoroutine<Any> { throw IndexOutOfBoundsException() }
        }
        invokingSuspend { func() } shouldThrow IndexOutOfBoundsException::class
    }

    @Test
    fun failWhenTestingASuspendFunctionThatDoesNotThrowTheExpectedException() = runBlockingTest {
        suspend fun func() {
            suspendCancellableCoroutine<Any> { throw IndexOutOfBoundsException() }
        }
        assertFails {
            invokingSuspend { func() } shouldThrow IllegalArgumentException::class
        }
    }

    @Test
    fun passWhenTestingASuspendFunctionThatTriesToGetAnOutOfIndexedItem() = runBlockingTest {
        suspend fun func() {
            suspendCancellableCoroutine<Int> { listOf(0)[-1] }
        }
        invokingSuspend { func() } shouldThrow IndexOutOfBoundsException::class
    }

    @Test
    fun passWhenTestingASuspendFunctionWhichThrowsASubtypeOfTheExpectedException() = runBlockingTest {
        suspend fun func() {
            suspendCancellableCoroutine<Any> { throw IllegalStateException() }
        }
        invokingSuspend { func() } shouldThrow RuntimeException::class
    }

    @Test
    fun passWhenTestingASuspendFunctionWhichThrowsAnExceptionWithTheExpectedMessage() = runBlockingTest {
        suspend fun func() {
            suspendCancellableCoroutine<Any> { throw Exception("Hello World!") }
        }
        invokingSuspend { func() } shouldThrow Exception::class withMessage "Hello World!"
    }

    @Test
    fun failWhenTestingASuspendFunctionWhichThrowsAnExceptionWithADifferentMessage() = runBlockingTest {
        suspend fun func() {
            suspendCancellableCoroutine<Any> { throw Exception("Hello World!") }
        }
        assertFails {
            invokingSuspend { func() } shouldThrow Exception::class withMessage "Hello"
        }
    }

    @Test
    fun passWhenTestingASuspendFunctionWhichThrowsAnExceptionWithTheExpectedCause() = runBlockingTest {
        suspend fun func() {
            suspendCancellableCoroutine<Any> { throw Exception(RuntimeException()) }
        }
        invokingSuspend { func() } shouldThrow Exception::class withCause RuntimeException::class
    }

    @Test
    fun failWhenTestingASuspendFunctionWhichThrowsAnExceptionWithAnUnexpectedCause() = runBlockingTest {
        suspend fun func() {
            suspendCancellableCoroutine<Any> { throw RuntimeException() }
        }
        assertFails {
            invokingSuspend { func() } shouldThrow Exception::class withCause IOException::class
        }
    }

    @Test
    fun passWhenTestingASuspendFunctionWhichThrowsWhenAnyExceptionIsExpected() = runBlockingTest {
        suspend fun func() {
            suspendCancellableCoroutine<Any> { throw Exception() }
        }
        invokingSuspend { func() } shouldThrow AnyException
    }

    @Test
    fun failWhenTestingASuspendFunctionWhichDoesNotThrowButAnyExceptionIsExpected() = runBlockingTest {
        suspend fun func() {
            coroutineScope { Unit }
        }
        assertFails { invokingSuspend { func() } shouldThrow AnyException }
    }

    @Test
    fun passWhenTestingASuspendFunctionWhichThrowsAnExceptionWithMessageAndCause() = runBlockingTest {
        suspend fun func() {
            suspendCancellableCoroutine<Any> { throw IllegalArgumentException("hello", IOException()) }
        }
        invokingSuspend { func() } shouldThrow
                IllegalArgumentException::class withCause IOException::class withMessage "hello"
    }

    @Test
    fun failWhenTestingASuspendFunctionWhichThrowsAnExceptionWithMessageAndCauseExceptingADifferentMessage() = runBlockingTest {
        suspend fun func() {
            suspendCancellableCoroutine<Any> { throw IllegalArgumentException("not hello", IOException()) }
        }
        assertFails {
            invokingSuspend { func() } shouldThrow
                    IllegalArgumentException::class withCause IOException::class withMessage "hello"
        }
    }

    @Test
    fun returnTheExceptionWhenPassingWithASuspendFunction() = runBlockingTest {
        suspend fun func() {
            suspendCancellableCoroutine<Any> { throw CustomException(10) }
        }

        val exception = invokingSuspend { func() }.shouldThrow(CustomException::class).exception

        exception.code.shouldEqual(10)
    }

    @Test
    fun passWhenTestingASuspendFunctionForAnExactThrownException() = runBlockingTest {
        suspend fun func() {
            suspendCancellableCoroutine<Any> { throw CustomException(12345) }
        }
        invokingSuspend { func() } shouldThrow CustomException(12345)
    }

    @Test
    fun failWhenTestingASuspendFunctionForAnExactThrownExceptionWhenTheExceptionDiffers() = runBlockingTest {
        suspend fun func() {
            suspendCancellableCoroutine<Any> { throw CustomException(12345) }
        }
        assertFails { invokingSuspend { func() } shouldThrow CustomException(54321) }
    }

    @Test
    fun passWhenTestingASuspendFunctionThatDoesNotThrowAnException() = runBlockingTest {
        suspend fun func() {
            coroutineScope { Unit }
        }
        invokingSuspend { func() } shouldNotThrow AnyException
    }

    @Test
    fun failWhenTestingASuspendFunctionThatDoesThrowAnException() = runBlockingTest {
        suspend fun func() {
            suspendCancellableCoroutine<Any> { throw IllegalArgumentException() }
        }
        assertFails {
            invokingSuspend { func() } shouldNotThrow AnyException
        }
    }

    @Test
    fun passWhenTestingASuspendFunctionThatDoesNotThrowTheExpectedException() = runBlockingTest {
        suspend fun func() {
            suspendCancellableCoroutine<Any> { throw IllegalArgumentException() }
        }
        invokingSuspend { func() } shouldNotThrow ArrayIndexOutOfBoundsException::class
    }

    @Test
    fun passWhenTestingASuspendFunctionThatReturnsNull() = runBlockingTest {
        suspend fun func(): String? {
            return coroutineScope { null }
        }
        invokingSuspend { func() } shouldNotThrow AnyException
    }

    @Test
    fun passWhenTestingASuspendFunctionThatThrowsAnExceptionWithADifferentMessage() = runBlockingTest {
        suspend fun func() {
            suspendCancellableCoroutine<Any> { throw IllegalArgumentException("Actual Message") }
        }
        invokingSuspend { func() } shouldNotThrow
                IllegalAccessException::class withMessage "Expected Message"
    }

    @Test
    fun failWhenTestingASuspendFunctionThatThrowsAnExceptionWithTheSameMessage() = runBlockingTest {
        suspend fun func() {
            suspendCancellableCoroutine<Any> { throw IllegalArgumentException("Actual Message") }
        }
        assertFails {
            invokingSuspend { func() } shouldNotThrow
                    IllegalAccessException::class withMessage "Actual Message"
        }
    }

    @Test
    fun failWhenTestingASuspendFunctionThatDoesThrowAnExceptionWithTheExpectedCause() = runBlockingTest {
        suspend fun func() {
            suspendCancellableCoroutine<Any> { throw Exception(RuntimeException()) }
        }
        assertFails {
            invokingSuspend { func() } shouldNotThrow
                    Exception::class withCause RuntimeException::class
        }
    }

    @Test
    fun failWhenTestingASuspendFunctionThatThrowsAnExpectedException() = runBlockingTest {
        suspend fun func() {
            suspendCancellableCoroutine<Any> { throw Exception() }
        }
        assertFails {
            invokingSuspend { func() } shouldNotThrow Exception::class
        }
    }
}

data class CustomException(val code: Int) : Exception("code is $code")
